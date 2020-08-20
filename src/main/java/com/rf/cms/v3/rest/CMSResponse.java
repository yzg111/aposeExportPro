package com.rf.cms.v3.rest;

/**
 * 功能描述:查询数据库数据返回接收数据的类
 */
public class CMSResponse {
  private String status;
  private String message;
  private Object content;
  private String statusCode = "1000";

  public String getStatus() {
    return this.status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getMessage() {
    return this.message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Object getContent() {
    return this.content;
  }

  public void setContent(Object content) {
    this.content = content;
  }

  public String getStatusCode() {
    return this.statusCode;
  }

  public void setStatusCode(String statusCode) {
    this.statusCode = statusCode;
  }
}
