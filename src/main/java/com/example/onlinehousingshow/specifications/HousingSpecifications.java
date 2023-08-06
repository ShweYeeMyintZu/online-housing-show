package com.example.onlinehousingshow.specifications;

import com.example.onlinehousingshow.model.Housing;
import org.springframework.data.jpa.domain.Specification;
import java.util.Date;

public class HousingSpecifications {

    public static Specification<Housing> forOwner(int ownerId) {
        return (root, query, builder) -> builder.equal(root.get("owner").get("id"), ownerId);
    }

    public static Specification<Housing> withHousingName(String housingName) {
        return (root, query, builder) -> builder.like(builder.lower(root.get("housingName")), "%" + housingName.toLowerCase() + "%");
    }

    public static Specification<Housing> withFloors(Integer floors) {
        return (root, query, builder) -> builder.equal(root.get("numberOfFloors"), floors);
    }

    public static Specification<Housing> withMasterRoom(Integer masterRoom) {
        return (root, query, builder) -> builder.equal(root.get("numberOfMasterRoom"), masterRoom);
    }

    public static Specification<Housing> withSingleRoom(Integer singleRoom) {
        return (root, query, builder) -> builder.equal(root.get("numberOfSingleRoom"), singleRoom);
    }

    public static Specification<Housing> withAmount(Double amount) {
        return (root, query, builder) -> builder.equal(root.get("amount"), amount);
    }

    public static Specification<Housing> withCreatedDate(Date createdDate) {
        return (root, query, builder) -> builder.equal(root.get("createdDate"), createdDate);
    }
}

