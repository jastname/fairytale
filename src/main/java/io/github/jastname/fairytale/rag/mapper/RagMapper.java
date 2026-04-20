package io.github.jastname.fairytale.rag.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import io.github.jastname.fairytale.rag.dto.RagChunk;
import io.github.jastname.fairytale.rag.dto.RagChunkSource;
import io.github.jastname.fairytale.rag.dto.RagEmbed;

@Mapper
public interface RagMapper {
	
	//청킹용 동화데이터 조회
	List<RagChunkSource> selectAllFairytaleForChunk();
	
	//청크 저장
	int insertChunk(RagChunk chunk);
	
	int deleteChunkAll();

	int insertEmbeded(RagEmbed Embed);
	
	int deleteEmbeddedAll();
}
