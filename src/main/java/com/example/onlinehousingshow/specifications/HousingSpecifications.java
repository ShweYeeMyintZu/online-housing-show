package com.example.onlinehousingshow.specifications;

import com.example.onlinehousingshow.model.Housing;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

public class HousingSpecifications {

    public static Specification<Housing> withHousingName(Optional<String> housingName) {
        if (housingName.filter(StringUtils::hasLength).isPresent()) {
            return (root, query, cb) -> cb.like(cb.lower(root.get("housingName")), housingName.get().toLowerCase().concat("%"));
        }

        return Specification.where(null);
    }

    public static Specification<Housing> withFloors(Optional<Integer> floors) {
        if (floors.filter(a -> a > 0).isPresent()) {
            return (root, query, cb) -> cb.equal(root.get("numberOfFloors"), floors.get());
        }
        return Specification.where(null);
    }

    public static Specification<Housing> withMasterRoom(Optional<Integer> masterRoom) {
        if (masterRoom.filter(a -> a > 0).isPresent()) {
            return (root, query, cb) -> cb.equal(root.get("numberOfMasterRoom"), masterRoom.get());
        }
        return Specification.where(null);
    }

    public static Specification<Housing> withSingleRoom(Optional<Integer> singleRoom) {
        if (singleRoom.filter(a -> a > 0).isPresent()) {
            return (root, query, cb) -> cb.equal(root.get("numberOfSingleRoom"), singleRoom.get());
        }
        return Specification.where(null);
    }

    public static Specification<Housing> withAmount(Optional<Double> amount) {
        if (amount.filter(a -> a > 0).isPresent()) {
            return (root, query, cb) -> cb.equal(root.get("amount"), amount.get());
        }
        return Specification.where(null);
    }

    public static Specification<Housing> withCreatedDate(Optional<Date> createdDate) {
        if (createdDate.isPresent()) {
            return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("createdDate"), createdDate.get());
        }

        return Specification.where(null);
    }
}
