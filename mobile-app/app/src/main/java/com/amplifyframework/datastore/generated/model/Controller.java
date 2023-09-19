package com.amplifyframework.datastore.generated.model;


import androidx.core.util.ObjectsCompat;

import java.util.Objects;
import java.util.List;

/** This is an auto generated class representing the Controller type in your schema. */
public final class Controller {
  private final String fanSpeed;
  private final String swaying;
  private final Boolean playingMusic;
  private final Boolean humidifier;
  private final Double acTemp;
  public String getFanSpeed() {
      return fanSpeed;
  }
  
  public String getSwaying() {
      return swaying;
  }
  
  public Boolean getPlayingMusic() {
      return playingMusic;
  }
  
  public Boolean getHumidifier() {
      return humidifier;
  }
  
  public Double getAcTemp() {
      return acTemp;
  }
  
  private Controller(String fanSpeed, String swaying, Boolean playingMusic, Boolean humidifier, Double acTemp) {
    this.fanSpeed = fanSpeed;
    this.swaying = swaying;
    this.playingMusic = playingMusic;
    this.humidifier = humidifier;
    this.acTemp = acTemp;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Controller controller = (Controller) obj;
      return ObjectsCompat.equals(getFanSpeed(), controller.getFanSpeed()) &&
              ObjectsCompat.equals(getSwaying(), controller.getSwaying()) &&
              ObjectsCompat.equals(getPlayingMusic(), controller.getPlayingMusic()) &&
              ObjectsCompat.equals(getHumidifier(), controller.getHumidifier()) &&
              ObjectsCompat.equals(getAcTemp(), controller.getAcTemp());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getFanSpeed())
      .append(getSwaying())
      .append(getPlayingMusic())
      .append(getHumidifier())
      .append(getAcTemp())
      .toString()
      .hashCode();
  }
  
  public static BuildStep builder() {
      return new Builder();
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(fanSpeed,
      swaying,
      playingMusic,
      humidifier,
      acTemp);
  }
  public interface BuildStep {
    Controller build();
    BuildStep fanSpeed(String fanSpeed);
    BuildStep swaying(String swaying);
    BuildStep playingMusic(Boolean playingMusic);
    BuildStep humidifier(Boolean humidifier);
    BuildStep acTemp(Double acTemp);
  }
  

  public static class Builder implements BuildStep {
    private String fanSpeed;
    private String swaying;
    private Boolean playingMusic;
    private Boolean humidifier;
    private Double acTemp;
    @Override
     public Controller build() {
        
        return new Controller(
          fanSpeed,
          swaying,
          playingMusic,
          humidifier,
          acTemp);
    }
    
    @Override
     public BuildStep fanSpeed(String fanSpeed) {
        this.fanSpeed = fanSpeed;
        return this;
    }
    
    @Override
     public BuildStep swaying(String swaying) {
        this.swaying = swaying;
        return this;
    }
    
    @Override
     public BuildStep playingMusic(Boolean playingMusic) {
        this.playingMusic = playingMusic;
        return this;
    }
    
    @Override
     public BuildStep humidifier(Boolean humidifier) {
        this.humidifier = humidifier;
        return this;
    }
    
    @Override
     public BuildStep acTemp(Double acTemp) {
        this.acTemp = acTemp;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String fanSpeed, String swaying, Boolean playingMusic, Boolean humidifier, Double acTemp) {
      super.fanSpeed(fanSpeed)
        .swaying(swaying)
        .playingMusic(playingMusic)
        .humidifier(humidifier)
        .acTemp(acTemp);
    }
    
    @Override
     public CopyOfBuilder fanSpeed(String fanSpeed) {
      return (CopyOfBuilder) super.fanSpeed(fanSpeed);
    }
    
    @Override
     public CopyOfBuilder swaying(String swaying) {
      return (CopyOfBuilder) super.swaying(swaying);
    }
    
    @Override
     public CopyOfBuilder playingMusic(Boolean playingMusic) {
      return (CopyOfBuilder) super.playingMusic(playingMusic);
    }
    
    @Override
     public CopyOfBuilder humidifier(Boolean humidifier) {
      return (CopyOfBuilder) super.humidifier(humidifier);
    }
    
    @Override
     public CopyOfBuilder acTemp(Double acTemp) {
      return (CopyOfBuilder) super.acTemp(acTemp);
    }
  }
  
}
