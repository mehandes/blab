package org.blab.river;

public class Status {
  private Code code;
  private RiverException error;

  public Status() {
    this.code = Code.RUNNING;
    this.error = null;
  }

  public Code getCode() {
    return code;
  }

  public Exception getError() {
    return error;
  }

  public void setCorrupted(RiverException error) {
    this.code = Code.CORRUPTED;
    this.error = error;
  }

  public void setClosed() {
    this.code = Code.CLOSED;
    this.error = null;
  }

  public void setRunning() {
    this.code = Code.RUNNING;
    this.error = null;
  }

  public enum Code {
    RUNNING,
    CLOSED,
    CORRUPTED
  }
}
