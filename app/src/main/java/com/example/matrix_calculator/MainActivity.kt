package com.example.matrix_calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matrix_calculator.ui.theme.Matrix_calculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        System.loadLibrary("matrix_calculator")
        setContent{
            MatrixCalculatorUI()
        }
    }
}

@Composable
fun MatrixCalculatorUI(){

    var dimension by remember { mutableStateOf("") }
    var matrixA by remember { mutableStateOf("") }
    var matrixB by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var selectedOp by remember { mutableStateOf("Add") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(16.dp))
        {
            Text("Matrix Calculator", fontSize = 22.sp)
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = dimension,
                onValueChange = { dimension = it},
                label = { Text("Matrix Dimension n") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = matrixA,
                onValueChange = { matrixA = it},
                label = { Text("Matrix A, comma seperated") }
            )


            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = matrixB,
                onValueChange = { matrixB = it},
                label = { Text("Matrix B, comma seperated") }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row {
                listOf("Add", "Subtract", "Multiply" , "Divide").forEach{
                    Button(
                        onClick = {selectedOp = it},
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(it)
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            Button(onClick = {
                errorMessage = ""
                val dim = dimension.toIntOrNull();
                if(dim == null || dim <= 0){
                    errorMessage = "Incorrect Dimensions";
                    return@Button
                }
                val matrixAList = matrixA.split(",")
                val matrixBList = matrixB.split(",")

                if(matrixAList.size != dim*dim || matrixBList.size != dim*dim ){
                    errorMessage = "Matrix dimension are incorrect"
                    return@Button
                }

                val invalidA = matrixAList.any { it.trim().toDoubleOrNull() == null }
                val invalidB = matrixBList.any { it.trim().toDoubleOrNull() == null }

                if (invalidA || invalidB) {
                    errorMessage = "Matrix entries must be valid numbers."
                    return@Button
                }

                result = performMatrixOperation(matrixA, matrixB, dimension.toInt(), selectedOp)
            }) {
                Text("Calculate")
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text("Result: $result")

            if(errorMessage.isNotEmpty()){
                Text(text = errorMessage)
            }

        }
}

external fun performMatrixOperation(a: String, b: String, dim: Int, op: String): String



