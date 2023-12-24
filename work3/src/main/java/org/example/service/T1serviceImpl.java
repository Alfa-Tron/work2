package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.repository.T1repository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class T1serviceImpl implements T1service{
    private  final T1repository repository;
}
