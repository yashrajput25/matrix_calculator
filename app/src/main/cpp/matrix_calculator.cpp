

#include <jni.h>
#include <string>
#include <vector>
#include <sstream>
#include <iostream>

using namespace std;

vector<vector<int>> parseMatrix(const string& str, int dim){

    vector<vector<int>> matrix(dim, vector<int> (dim));
    stringstream ss(str);
    string num;
    int row = 0, col = 0;

    while(getline(ss, num, ',')){
        matrix[row][col] = stoi(num);
        col++;
        if(col == dim){
            col = 0;
            row++;
        }
    }
    return matrix;

}

string matrixToString(vector<vector<int>>& matrix){
    stringstream ss;
    for(int i = 0; i<matrix.size() ; i++){
        for(int j = 0; j<matrix.size() ; j++){
            ss<<matrix[i][j];
            if(i != matrix.size()-1 || j !=matrix.size()-1){
                ss<<",";
            }
        }
    }
    return ss.str();
}

extern "C"
JNIEXPORT jstring  JNICALL
//Java_com_example_matrix_1calculator_MainActivityKt_performMatrixOperation(JNIEnv *env, jclass clazz,
//                                                                          jstring matrix_a,
//                                                                          jstring matrix_b,
//                                                                          jint to_int,
//                                                                          jstring selected_op)
Java_com_example_matrix_1calculator_MainActivityKt_performMatrixOperation(
        JNIEnv* env,
        jobject,
        jstring matAStr,
        jstring matBStr,
        jint dim,
        jstring opStr){

    const char* aCStr = env->GetStringUTFChars(matAStr,0);
    const char* bCStr = env->GetStringUTFChars(matBStr,0);
    const char* opCStr = env->GetStringUTFChars(opStr, 0);

    string matrixA(aCStr);
    string matrixB(bCStr);
    string operation(opCStr);

    env->ReleaseStringUTFChars(matAStr, aCStr);
    env->ReleaseStringUTFChars(matBStr, bCStr);
    env->ReleaseStringUTFChars(opStr, opCStr);

    vector<vector<int>> A = parseMatrix(matrixA, dim);
    vector<vector<int>> B = parseMatrix(matrixB, dim);
    vector<vector<int>> result(dim, vector<int>(dim, 0));


    if(operation == "Add"){
        for(int i = 0; i< dim ; i++){
            for(int j = 0; j<dim; j++){
                result[i][j] = A[i][j] + B[i][j];

            }
        }
    }

    else if(operation == "Subtract"){
        for(int i = 0; i < dim; i++){
            for(int j = 0; j < dim; j++){
                result[i][j] = A[i][j]- B[i][j];
            }
        }
    }

    else if(operation == "Multiply"){
        for(int i =0; i<dim;i++){
            for(int j = 0; j<dim; j++){
                for(int k = 0; k<dim; k++){
                    result[i][j] += A[i][k]*B[k][j];
                }
            }
        }

    }

    else if(operation == "Divide"){
        for (int i = 0; i < dim; i++)
            for (int j = 0; j < dim; j++) {
                if (B[i][j] != 0)
                    result[i][j] = A[i][j] / B[i][j];
                else
                    result[i][j] = 0; // avoid division by zero
            }
    }

    string resultString = matrixToString(result);
    return env->NewStringUTF(resultString.c_str());
}

