package com.amplifyframework.datastore.generated.model;


import androidx.core.util.ObjectsCompat;

import java.util.Objects;
import java.util.List;

/** This is an auto generated class representing the Bracelet type in your schema. */
public final class Bracelet {
  private final Double heartBeats;
  private final Double oxygen;
  private final Double temperature;
  public Double getHeartBeats() {
      return heartBeats;
  }
  
  public Double getOxygen() {
      return oxygen;
  }
  
  public Double getTemperature() {
      return temperature;
  }
  
  private Bracelet(Double heartBeats, Double oxygen, Double temperature) {
    this.heartBeats = heartBeats;
    this.oxygen = oxygen;
    this.temperature = temperature;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Bracelet bracelet = (Bracelet) obj;
      return ObjectsCompat.equals(getHeartBeats(), bracelet.getHeartBeats()) &&
              ObjectsCompat.equals(getOxygen(), bracelet.getOxygen()) &&
              ObjectsCompat.equals(getTemperature(), bracelet.getTemperature());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getHeartBeats())
      .append(getOxygen())
      .append(getTemperature())
      .toString()
      .hashCode();
  }
  
  public static BuildStep builder() {
      return new Builder();
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(heartBeats,
      oxygen,
      temperature);
  }
  public interface BuildStep {
    Bracelet build();
    BuildStep heartBeats(Double heartBeats);
    BuildStep oxygen(Double oxygen);
    BuildStep temperature(Double temperature);
  }
  

  public static class Builder implements BuildStep {
    private Double heartBeats;
    private Double oxygen;
    private Double temperature;
    @Override
     public Bracelet build() {
        
        return new Bracelet(
          heartBeats,
          oxygen,
          temperature);
    }
    
    @Override
     public BuildStep heartBeats(Double heartBeats) {
        this.heartBeats = heartBeats;
        return this;
    }
    
    @Override
     public BuildStep oxygen(Double oxygen) {
        this.oxygen = oxygen;
        return this;
    }
    
    @Override
     public BuildStep temperature(Double temperature) {
        this.temperature = temperature;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(Double heartBeats, Double oxygen, Double temperature) {
      super.heartBeats(heartBeats)
        .oxygen(oxygen)
        .temperature(temperature);
    }
    
    @Override
     public CopyOfBuilder heartBeats(Double heartBeats) {
      return (CopyOfBuilder) super.heartBeats(heartBeats);
    }
    
    @Override
     public CopyOfBuilder oxygen(Double oxygen) {
      return (CopyOfBuilder) super.oxygen(oxygen);
    }
    
    @Override
     public CopyOfBuilder temperature(Double temperature) {
      return (CopyOfBuilder) super.temperature(temperature);
    }
  }
  
}
