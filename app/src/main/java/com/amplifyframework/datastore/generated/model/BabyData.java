package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.temporal.Temporal;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.AuthStrategy;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelOperation;
import com.amplifyframework.core.model.annotations.AuthRule;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the BabyData type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "BabyData", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class BabyData implements Model {
  public static final QueryField ID = field("BabyData", "id");
  public static final QueryField DEVICE_ID = field("BabyData", "deviceId");
  public static final QueryField TIMESTAMP = field("BabyData", "timestamp");
  public static final QueryField BRACELET = field("BabyData", "bracelet");
  public static final QueryField CRADLE = field("BabyData", "cradle");
  public static final QueryField CONTROLLER = field("BabyData", "controller");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String deviceId;
  private final @ModelField(targetType="Int", isRequired = true) Integer timestamp;
  private final @ModelField(targetType="Bracelet") Bracelet bracelet;
  private final @ModelField(targetType="Cradle") Cradle cradle;
  private final @ModelField(targetType="Controller") Controller controller;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String resolveIdentifier() {
    return id;
  }
  
  public String getId() {
      return id;
  }
  
  public String getDeviceId() {
      return deviceId;
  }
  
  public Integer getTimestamp() {
      return timestamp;
  }
  
  public Bracelet getBracelet() {
      return bracelet;
  }
  
  public Cradle getCradle() {
      return cradle;
  }
  
  public Controller getController() {
      return controller;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private BabyData(String id, String deviceId, Integer timestamp, Bracelet bracelet, Cradle cradle, Controller controller) {
    this.id = id;
    this.deviceId = deviceId;
    this.timestamp = timestamp;
    this.bracelet = bracelet;
    this.cradle = cradle;
    this.controller = controller;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      BabyData babyData = (BabyData) obj;
      return ObjectsCompat.equals(getId(), babyData.getId()) &&
              ObjectsCompat.equals(getDeviceId(), babyData.getDeviceId()) &&
              ObjectsCompat.equals(getTimestamp(), babyData.getTimestamp()) &&
              ObjectsCompat.equals(getBracelet(), babyData.getBracelet()) &&
              ObjectsCompat.equals(getCradle(), babyData.getCradle()) &&
              ObjectsCompat.equals(getController(), babyData.getController()) &&
              ObjectsCompat.equals(getCreatedAt(), babyData.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), babyData.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getDeviceId())
      .append(getTimestamp())
      .append(getBracelet())
      .append(getCradle())
      .append(getController())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("BabyData {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("deviceId=" + String.valueOf(getDeviceId()) + ", ")
      .append("timestamp=" + String.valueOf(getTimestamp()) + ", ")
      .append("bracelet=" + String.valueOf(getBracelet()) + ", ")
      .append("cradle=" + String.valueOf(getCradle()) + ", ")
      .append("controller=" + String.valueOf(getController()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static DeviceIdStep builder() {
      return new Builder();
  }
  
  /**
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static BabyData justId(String id) {
    return new BabyData(
      id,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      deviceId,
      timestamp,
      bracelet,
      cradle,
      controller);
  }
  public interface DeviceIdStep {
    TimestampStep deviceId(String deviceId);
  }
  

  public interface TimestampStep {
    BuildStep timestamp(Integer timestamp);
  }
  

  public interface BuildStep {
    BabyData build();
    BuildStep id(String id);
    BuildStep bracelet(Bracelet bracelet);
    BuildStep cradle(Cradle cradle);
    BuildStep controller(Controller controller);
  }
  

  public static class Builder implements DeviceIdStep, TimestampStep, BuildStep {
    private String id;
    private String deviceId;
    private Integer timestamp;
    private Bracelet bracelet;
    private Cradle cradle;
    private Controller controller;
    @Override
     public BabyData build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new BabyData(
          id,
          deviceId,
          timestamp,
          bracelet,
          cradle,
          controller);
    }
    
    @Override
     public TimestampStep deviceId(String deviceId) {
        Objects.requireNonNull(deviceId);
        this.deviceId = deviceId;
        return this;
    }
    
    @Override
     public BuildStep timestamp(Integer timestamp) {
        Objects.requireNonNull(timestamp);
        this.timestamp = timestamp;
        return this;
    }
    
    @Override
     public BuildStep bracelet(Bracelet bracelet) {
        this.bracelet = bracelet;
        return this;
    }
    
    @Override
     public BuildStep cradle(Cradle cradle) {
        this.cradle = cradle;
        return this;
    }
    
    @Override
     public BuildStep controller(Controller controller) {
        this.controller = controller;
        return this;
    }
    
    /**
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String deviceId, Integer timestamp, Bracelet bracelet, Cradle cradle, Controller controller) {
      super.id(id);
      super.deviceId(deviceId)
        .timestamp(timestamp)
        .bracelet(bracelet)
        .cradle(cradle)
        .controller(controller);
    }
    
    @Override
     public CopyOfBuilder deviceId(String deviceId) {
      return (CopyOfBuilder) super.deviceId(deviceId);
    }
    
    @Override
     public CopyOfBuilder timestamp(Integer timestamp) {
      return (CopyOfBuilder) super.timestamp(timestamp);
    }
    
    @Override
     public CopyOfBuilder bracelet(Bracelet bracelet) {
      return (CopyOfBuilder) super.bracelet(bracelet);
    }
    
    @Override
     public CopyOfBuilder cradle(Cradle cradle) {
      return (CopyOfBuilder) super.cradle(cradle);
    }
    
    @Override
     public CopyOfBuilder controller(Controller controller) {
      return (CopyOfBuilder) super.controller(controller);
    }
  }
  
}
