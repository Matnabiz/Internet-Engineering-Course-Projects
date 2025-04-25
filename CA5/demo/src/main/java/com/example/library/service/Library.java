package com.example.library.service;

import com.example.library.repository.Repository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.example.library.model.User;
import com.example.library.model.Author;
import com.example.library.model.Book;
import com.example.library.model.Order;
import com.example.library.model.Comment;
import com.example.library.dto.ResponseWrapper;
import com.example.library.model.Address;
@Service

public class Library {

    private Repository systemData;
    private boolean success;
    private  String message;
    private List<Object> data;


}
