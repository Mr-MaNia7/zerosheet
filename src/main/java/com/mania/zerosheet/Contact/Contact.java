package com.mania.zerosheet.Contact;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Contact implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long commentId;

    @NotBlank(message = "Comment is required")
    private String comment;

    @Email(message = "Please enter a valid e-mail address")
    @NotBlank(message = "Email Address is required")
    private String email;

    @NotBlank(message = "First Name is required")
    private String fname;

    public Contact(String comment, String email, String fname) {
        this.comment = comment;
        this.email = email;
        this.fname = fname;
    }

}
