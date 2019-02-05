/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Usuario
 */
public class SessionModel extends ModelClass{
    private String username, name, email;
    private int id, typeuser;
    private boolean blocked_status;

    public boolean isBlocked_status() {
        return blocked_status;
    }

    public void setBlocked_status(boolean blocked_status) {
        this.blocked_status = blocked_status;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTypeuser() {
        return typeuser;
    }

    public void setTypeuser(int typeuser) {
        this.typeuser = typeuser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
