package io.github.jastname.fairytale.rag.dto;

public class ragChunkSource {
	
	private String fairytaleId;
	private String collectId;
	private String title;
	private String description;
	
	public String getFairytaleId() {
		return fairytaleId;
	}
	public void setFairytaleId(String fairytaleId) {
		this.fairytaleId = fairytaleId;
	}
	
	public String getCollectId() {
		return collectId;
	}
	public void setCollectId(String collectId) {
		this.collectId = collectId;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}