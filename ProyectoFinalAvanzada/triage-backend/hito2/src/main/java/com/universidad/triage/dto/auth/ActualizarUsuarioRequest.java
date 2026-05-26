package com.universidad.triage.dto.request;

public class ActualizarUsuarioRequest {

    private Boolean activo;
    private String nombre;
    private String email;

    public ActualizarUsuarioRequest() {}

    public Boolean getActivo() { return activo; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }

    public void setActivo(Boolean activo) { this.activo = activo; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEmail(String email) { this.email = email; }
}
