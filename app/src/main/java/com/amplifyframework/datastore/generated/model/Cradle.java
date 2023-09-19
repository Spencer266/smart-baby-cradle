package com.amplifyframework.datastore.generated.model;


import androidx.core.util.ObjectsCompat;

import java.util.Objects;
import java.util.List;

/** This is an auto generated class representing the Cradle type in your schema. */
public final class Cradle {
  private final Double environmentTemp;
  private final Double humidity;
  private final String cry;
  public Double getEnvironmentTemp() {
      return environmentTemp;
  }
  
  public Double getHumidity() {
      return humidity;
  }
  
  public String getCry() {
      return cry;
  }
  
  private Cradle(Double environmentTemp, Double humidity, String cry) {
    this.environmentTemp = environmentTemp;
    this.humidity = humidity;
    this.cry = cry;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Cradle cradle = (Cradle) obj;
      return ObjectsCompat.equals(getEnvironmentTemp(), cradle.getEnvironmentTemp()) &&
              ObjectsCompat.equals(getHumidity(), cradle.getHumidity()) &&
              ObjectsCompat.equals(getCry(), cradle.getCry());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getEnvironmentTemp())
      .append(getHumidity())
      .append(getCry())
      .toString()
      .hashCode();
  }
  
  public static BuildStep builder() {
      return new Builder();
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(environmentTemp,
      humidity,
      cry);
  }
  public interface BuildStep {
    Cradle build();
    BuildStep environmentTemp(Double environmentTemp);
    BuildStep humidity(Double humidity);
    BuildStep cry(String cry);
  }
  

  public static class Builder implements BuildStep {
    private Double environmentTemp;
    private Double humidity;
    private String cry;
    @Override
     public Cradle build() {
        
        return new Cradle(
          environmentTemp,
          humidity,
          cry);
    }
    
    @Override
     public BuildStep environmentTemp(Double environmentTemp) {
        this.environmentTemp = environmentTemp;
        return this;
    }
    
    @Override
     public BuildStep humidity(Double humidity) {
        this.humidity = humidity;
        return this;
    }
    
    @Override
     public BuildStep cry(String cry) {
        this.cry = cry;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(Double environmentTemp, Double humidity, String cry) {
      super.environmentTemp(environmentTemp)
        .humidity(humidity)
        .cry(cry);
    }
    
    @Override
     public CopyOfBuilder environmentTemp(Double environmentTemp) {
      return (CopyOfBuilder) super.environmentTemp(environmentTemp);
    }
    
    @Override
     public CopyOfBuilder humidity(Double humidity) {
      return (CopyOfBuilder) super.humidity(humidity);
    }
    
    @Override
     public CopyOfBuilder cry(String cry) {
      return (CopyOfBuilder) super.cry(cry);
    }
  }
  
}
