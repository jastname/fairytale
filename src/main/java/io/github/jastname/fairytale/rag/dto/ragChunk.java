package io.github.jastname.fairytale.rag.dto;

public class ragChunk {

	private String chunkId;
	private String fairytaleId;
	private String title;
	private int chunkNo;
	private String chunkText;

	public String getChunkId() {
		return chunkId;
	}
	public void setChunkId(String chunkId) {
		this.chunkId = chunkId;
	}

	public String getFairytaleId() {
		return fairytaleId;
	}
	public void setFairytaleId(String fairytaleId) {
		this.fairytaleId = fairytaleId;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public int getChunkNo() {
		return chunkNo;
	}
	public void setChunkNo(int chunkNo) {
		this.chunkNo = chunkNo;
	}

	public String getChunkText() {
		return chunkText;
	}
	public void setChunkText(String chunkText) {
		this.chunkText = chunkText;
	}

}