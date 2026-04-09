package io.github.jastname.fairytale.rag.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jastname.fairytale.rag.dto.ragChunk;
import io.github.jastname.fairytale.rag.dto.ragEmbed;
import io.github.jastname.fairytale.rag.mapper.ragMapper;
import io.github.jastname.fairytale.rag.service.embeddingService;
import io.github.jastname.fairytale.utill.CommonUtil;
import lombok.RequiredArgsConstructor;

@Service("embeddingService")
@RequiredArgsConstructor
public class embeddingServiceImpl implements embeddingService {

    private static final Logger log = LoggerFactory.getLogger(embeddingServiceImpl.class);
    private static final long PAGE_DELAY_MS = 500L;

    private final ragMapper mapper;
    private final EmbeddingModel embeddingModel;

    @Value("${spring.ai.ollama.embedding.options.model}")
    private String embeddingModelName;
    
    @Value("${app.rag.chunk-size}")
    private int chunkSize;
    
    @Value("${app.rag.chunk-overlap}")
    private int chunkOverlap;
    
    @Override
    public double[] embed(String text) {
        String prompt = text == null ? "" : text;

        EmbeddingResponse response;
        try {
            response = embeddingModel.embedForResponse(List.of(prompt));
        } catch (Exception e) {
            throw new IllegalStateException("Spring AI EmbeddingModel 호출에 실패했습니다.", e);
        }

        if (response == null || response.getResults() == null || response.getResults().isEmpty()) {
            throw new IllegalStateException("임베딩 응답에 embedding 값이 없습니다.");
        }

        float[] floatVector = response.getResults().get(0).getOutput();
        return toDoubleArray(floatVector);
    }

    @Override
    public String modelName() {
        return embeddingModelName;
    }

    private double[] toDoubleArray(float[] floats) {
        double[] doubles = new double[floats.length];
        for (int i = 0; i < floats.length; i++) {
            doubles[i] = floats[i];
        }
        return doubles;
    }

    @Override
    @Transactional
    public String chunkAndEmbedAllFairytales() {
        //이전 적재 내용 제거
    	mapper.deleteEmbeddedAll();
    	mapper.deleteChunkAll();        

        //청킹용 동화데이터 조회
        var chunkSources = mapper.selectAllFairytaleForChunk();
        log.info("[청킹용 데이터 조회] count={}", chunkSources.size());

        log.info("[청킹 설정] chunkSize={}, chunkOverlap={}", chunkSize, chunkOverlap);

        //청킹 및 임베딩 처리
        for(var source : chunkSources) {

            String description = source.getDescription();
            if(description == null || description.isBlank()) {
                log.warn("[청킹 건너뜀] description 없음 fairytaleId={}", source.getFairytaleId());
                continue;
            }

            List<String> chunks = splitIntoChunks(description, chunkSize, chunkOverlap);
            log.info("[청킹및 임베딩] fairytaleId={}, collectId={}, 청크수={}", source.getFairytaleId(), source.getCollectId(), chunks.size());

            //제목 임베딩 처리
            double[] embededTitle = embed(source.getTitle());
            
            for(int seq = 0; seq < chunks.size(); seq++) {
                ragChunk chunk = new ragChunk();
                chunk.setChunkId(CommonUtil.idMake("CHK"));
                chunk.setFairytaleId(source.getFairytaleId());
                chunk.setTitle(source.getTitle());
                chunk.setChunkNo(seq);
                chunk.setChunkText(chunks.get(seq));

                mapper.insertChunk(chunk);

                //임베딩 처리
                double[] embededText = embed(chunks.get(seq));
                
                ragEmbed embed = new ragEmbed();
                embed.setEmbeddingId(CommonUtil.idMake("EMB"));
                embed.setChunkId(chunk.getChunkId());
                embed.setEmbeddingModel(modelName());
                embed.setTitleVector(toVectorString(embededTitle));
                embed.setEmbeddingVector(toVectorString(embededText));
                
                mapper.insertEmbeded(embed);
            }
        }

        return "success";
    }

    /**
     * double[] → "[0.1,0.2,...]" PostgreSQL vector 문자열로 변환
     */
    private String toVectorString(double[] vector) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < vector.length; i++) {
            if (i > 0) sb.append(",");
            sb.append(vector[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * text를 chunkSize 단위로 자르되, chunkOverlap만큼 앞 내용을 겹쳐서 청킹
     */
    private List<String> splitIntoChunks(String text, int chunkSize, int chunkOverlap) {
        List<String> result = new ArrayList<>();
        int length = text.length();
        int start  = 0;
        while(start < length) {
            int end = Math.min(start + chunkSize, length);
            result.add(text.substring(start, end));
            if(end == length) break;
            start = end - chunkOverlap;
        }
        return result;
    }

}