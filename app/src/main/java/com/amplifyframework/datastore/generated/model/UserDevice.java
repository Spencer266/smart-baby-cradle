package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.core.model.ModelIdentifier;

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

/** This is an auto generated class representing the UserDevice type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "UserDevices", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class UserDevice implements Model {
  public static final QueryField ID = field("UserDevice", "id");
  public static final QueryField USER_ID = field("UserDevice", "userID");
  public static final QueryField DEVICE_ID = field("UserDevice", "deviceId");
  public static final QueryField MAPPING_NAME = field("UserDevice", "mappingName");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String userID;
  private final @ModelField(targetType="String", isRequired = true) String deviceId;
  private final @ModelField(targetType="String") String mappingName;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  /** @deprecated This API is internal to Amplify and should not be used. */
  @Deprecated
   public String resolveIdentifier() {
    return id;
  }
  
  public String getId() {
      return id;
  }
  
  public String getUserId() {
      return userID;
  }
  
  public String getDeviceId() {
      return deviceId;
  }
  
  public String getMappingName() {
      return mappingName;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private UserDevice(String id, String userID, String deviceId, String mappingName) {
    this.id = id;
    this.userID = userID;
    this.deviceId = deviceId;
    this.mappingName = mappingName;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      UserDevice userDevice = (UserDevice) obj;
      return ObjectsCompat.equals(getId(), userDevice.getId()) &&
              ObjectsCompat.equals(getUserId(), userDevice.getUserId()) &&
              ObjectsCompat.equals(getDeviceId(), userDevice.getDeviceId()) &&
              ObjectsCompat.equals(getMappingName(), userDevice.getMappingName()) &&
              ObjectsCompat.equals(getCreatedAt(), userDevice.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), userDevice.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUserId())
      .append(getDeviceId())
      .append(getMappingName())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("UserDevice {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("userID=" + String.valueOf(getUserId()) + ", ")
      .append("deviceId=" + String.valueOf(getDeviceId()) + ", ")
      .append("mappingName=" + String.valueOf(getMappingName()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static UserIdStep builder() {
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
  public static UserDevice justId(String id) {
    return new UserDevice(
      id,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      userID,
      deviceId,
      mappingName);
  }
  public interface UserIdStep {
    DeviceIdStep userId(String userId);
  }
  

  public interface DeviceIdStep {
    BuildStep deviceId(String deviceId);
  }
  

  public interface BuildStep {
    UserDevice build();
    BuildStep id(String id);
    BuildStep mappingName(String mappingName);
  }
  

  public static class Builder implements UserIdStep, DeviceIdStep, BuildStep {
    private String id;
    private String userID;
    private String deviceId;
    private String mappingName;
    @Override
     public UserDevice build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new UserDevice(
          id,
          userID,
          deviceId,
          mappingName);
    }
    
    @Override
     public DeviceIdStep userId(String userId) {
        Objects.requireNonNull(userId);
        this.userID = userId;
        return this;
    }
    
    @Override
     public BuildStep deviceId(String deviceId) {
        Objects.requireNonNull(deviceId);
        this.deviceId = deviceId;
        return this;
    }
    
    @Override
     public BuildStep mappingName(String mappingName) {
        this.mappingName = mappingName;
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
    private CopyOfBuilder(String id, String userId, String deviceId, String mappingName) {
      super.id(id);
      super.userId(userId)
        .deviceId(deviceId)
        .mappingName(mappingName);
    }
    
    @Override
     public CopyOfBuilder userId(String userId) {
      return (CopyOfBuilder) super.userId(userId);
    }
    
    @Override
     public CopyOfBuilder deviceId(String deviceId) {
      return (CopyOfBuilder) super.deviceId(deviceId);
    }
    
    @Override
     public CopyOfBuilder mappingName(String mappingName) {
      return (CopyOfBuilder) super.mappingName(mappingName);
    }
  }
  

  public static class UserDeviceIdentifier extends ModelIdentifier<UserDevice> {
    private static final long serialVersionUID = 1L;
    public UserDeviceIdentifier(String id) {
      super(id);
    }
  }
  
}
