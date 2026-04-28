package pe.edu.vallegrande.wines.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@Document(collection = "customer")
public class Customer {

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("last_name")
    private String lastName;

    @Field("phone")
    private String phone;

    @Field("email")
    private String email;

    @Field("address")
    private String address;

    @Field("state")
    private String state; // "Activo" / "Inactivo"

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;
}
