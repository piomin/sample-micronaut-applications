package pl.piomin.services.model;

import javax.validation.constraints.*;

public class Person {

    @Max(10000)
    private Integer id;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @PositiveOrZero
    private int age;
    @NotNull
    private Gender gender;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
