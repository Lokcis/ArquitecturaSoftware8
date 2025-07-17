package com.example.models;

import java.io.Serializable;
import javax.persistence.*; // Importa todas las clases de persistencia
import java.util.Calendar; // Para las fechas createdAt y updatedAt

@Entity // Marca esta clase como una entidad JPA
@Table(name = "competitor") // Mapea la entidad a la tabla "competitor" en la base de datos
public class Competitor implements Serializable {

    @Id // Marca el campo 'id' como la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generación automática de ID por la base de datos
    private Long id;

    // Campos de la entidad
    private String name;
    private String surname;
    private String email;
    private String password; // ¡Cuidado! En una aplicación real, las contraseñas deben ser hasheadas
    private String cellphone;
    private String telephone;
    private String address;
    private String city;
    private String country;
    private int age;

    // Relación One-to-One con la entidad Vehicle
    // CascadeType.ALL significa que operaciones (persistir, actualizar, eliminar) se propagarán al Vehicle asociado.
    // orphanRemoval = true significa que si un Vehicle es desvinculado de un Competitor, será eliminado.
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    // @JoinColumn define la columna de clave foránea en la tabla 'competitor'
    // 'vehicle_id' es el nombre de la columna en la tabla 'competitor'
    // 'id' es la columna a la que hace referencia en la tabla 'vehicle'
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
    private Vehicle vehicle;

    // Relación One-to-One con la entidad Producto (ajusta a ManyToOne si es necesario)
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "producto_id", referencedColumnName = "id")
    private Producto producto;

    @Temporal(TemporalType.DATE) // Especifica que es un campo de fecha (sin hora)
    @Column(name = "create_at", updatable = false) // Mapea a la columna 'create_at', no actualizable
    private Calendar createdAt;

    @Temporal(TemporalType.DATE) // Especifica que es un campo de fecha (sin hora)
    @Column(name = "updated_at") // Mapea a la columna 'updated_at'
    private Calendar updatedAt;

    // Constructor vacío (requerido por JPA)
    public Competitor() {
    }

    // Método que se ejecuta antes de persistir la entidad (insertar en DB)
    @PrePersist
    protected void onCreate() {
        Calendar now = Calendar.getInstance();
        this.createdAt = now;
        this.updatedAt = now;
    }

    // Método que se ejecuta antes de actualizar la entidad
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Calendar.getInstance();
    }

    // --- Getters y Setters para todos los campos ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Calendar createdAt) {
        this.createdAt = createdAt;
    }

    public Calendar getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Calendar updatedAt) {
        this.updatedAt = updatedAt;
    }
}
