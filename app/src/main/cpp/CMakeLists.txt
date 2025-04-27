
cmake_minimum_required(VERSION 3.22.1)


project("matrix_calculator")

add_library(matrix_calculator SHARED

    matrix_calculator.cpp)

target_link_libraries(matrix_calculator
    ${log-lib})
