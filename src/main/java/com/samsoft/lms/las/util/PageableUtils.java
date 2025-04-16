package com.samsoft.lms.las.util;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PageableUtils {
	
	public  <T> Page<T> convertToPage(List<T> list, Pageable pageable) {
        Comparator<T> comparator = getComparator(pageable.getSort());

        if (comparator != null) {
            list.sort(comparator);
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());

        return new PageImpl<>(list.subList(start, end), pageable, list.size());
    }

	private <T> Comparator<T> getComparator(Sort sort) {
	    Comparator<T> comparator = null;

	    if (sort.isSorted()) {
	        Sort.Order order = sort.iterator().next(); // Get the first sorting order
	        String sortField = order.getProperty();

	        comparator = Comparator.comparing(obj -> {
	            try {
	                Field field = obj.getClass().getDeclaredField(sortField);
	                field.setAccessible(true);
	                Object value = field.get(obj);
	                // If the value is null, provide a default value for sorting purposes
	                return (Comparable) (value != null ? value : getDefaultComparableValue(field.getType()));
	            } catch (NoSuchFieldException | IllegalAccessException e) {
	                e.printStackTrace();
	                return null;
	            }
	        });

	        if (order.getDirection() == Sort.Direction.DESC) {
	            comparator = comparator.reversed();
	        }
	    }

	    return comparator;
	}

	// Provide default comparable values for different types
	private Comparable<?> getDefaultComparableValue(Class<?> type) {
	    if (type.equals(String.class)) {
	        return "";
	    } else if (type.equals(Integer.class) || type.equals(Long.class) || type.equals(Double.class)) {
	        return 0;
	    } else if (type.equals(LocalDate.class)) {
	        return LocalDate.MIN; // Default to minimum LocalDate value
	    } else if (type.equals(Instant.class)) {
	        return Instant.EPOCH; // Default to the epoch time (January 1, 1970)
	    }
	   
	    return null;
	}
	

}
