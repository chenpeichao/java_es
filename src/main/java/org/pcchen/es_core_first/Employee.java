package org.pcchen.es_core_first;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Ô±¹¤bean
 *
 * @author cpc
 * @create 2018-11-26 16:05
 **/
public class Employee {
    @JSONField(serialize = false)
    private String id;
    private String name;
    private Integer age;
    private String position;
    private String country;
    private String joinDate;
    private Long salary;

    public Employee() {
    }

    public Employee(String id, String name, Integer age, String position, String country, String joinDate, Long salary) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.position = position;
        this.country = country;
        this.joinDate = joinDate;
        this.salary = salary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public Long getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Employee employee = (Employee) o;

        if (id != null ? !id.equals(employee.id) : employee.id != null) return false;
        if (name != null ? !name.equals(employee.name) : employee.name != null) return false;
        if (age != null ? !age.equals(employee.age) : employee.age != null) return false;
        if (position != null ? !position.equals(employee.position) : employee.position != null) return false;
        if (country != null ? !country.equals(employee.country) : employee.country != null) return false;
        if (joinDate != null ? !joinDate.equals(employee.joinDate) : employee.joinDate != null) return false;
        return salary != null ? salary.equals(employee.salary) : employee.salary == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (age != null ? age.hashCode() : 0);
        result = 31 * result + (position != null ? position.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (joinDate != null ? joinDate.hashCode() : 0);
        result = 31 * result + (salary != null ? salary.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", position='" + position + '\'' +
                ", country='" + country + '\'' +
                ", joinDate='" + joinDate + '\'' +
                ", salary=" + salary +
                '}';
    }
}
