package io.github.jastname.fairytale.rag.dto;

public class ragEmbed {

	private String embeddingId;
	private String chunkId;
	private String embeddingModel;
	private String titleVector;      // "[0.1,0.2,...]" 형태 문자열
	private String embeddingVector;  // "[0.1,0.2,...]" 형태 문자열

	public String getEmbeddingId() { return embeddingId; }
	public void setEmbeddingId(String embeddingId) { this.embeddingId = embeddingId; }

	public String getChunkId() { return chunkId; }
	public void setChunkId(String chunkId) { this.chunkId = chunkId; }

	public String getEmbeddingModel() { return embeddingModel; }
	public void setEmbeddingModel(String embeddingModel) { this.embeddingModel = embeddingModel; }

	public String getTitleVector() { return titleVector; }
	public void setTitleVector(String titleVector) { this.titleVector = titleVector; }

	public String getEmbeddingVector() { return embeddingVector; }
	public void setEmbeddingVector(String embeddingVector) { this.embeddingVector = embeddingVector; }

}