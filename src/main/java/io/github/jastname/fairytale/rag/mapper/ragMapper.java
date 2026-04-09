package io.github.jastname.fairytale.rag.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import io.github.jastname.fairytale.rag.dto.ragChunk;
import io.github.jastname.fairytale.rag.dto.ragChunkSource;
import io.github.jastname.fairytale.rag.dto.ragEmbed;

@Mapper
public interface ragMapper {
	
	//청킹용 동화데이터 조회
	List<ragChunkSource> selectAllFairytaleForChunk();
	
	//청크 저장
	int insertChunk(ragChunk chunk);
	
	int deleteChunkAll();

	int insertEmbeded(ragEmbed Embed);
	
	int deleteEmbeddedAll();
}
