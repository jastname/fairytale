package io.github.jastname.fairytale.rag.safeguard.dto;

public class SafeguardResult {

    private boolean safe;
    private String code;
    private String reason;

    public SafeguardResult() {}

    public boolean isSafe() {
        return safe;
    }

    public void setSafe(boolean safe) {
        this.safe = safe;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}