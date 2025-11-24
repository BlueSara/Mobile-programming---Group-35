package com.example.studygroup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.studygroup.ui.theme.LocalCustomColors
import com.example.studygroup.ui.theme.LocalSpacing
import components.AppButton
import components.ButtonType
import components.TextInput

@Composable
fun SignIn(){
    val space = LocalSpacing.current
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = space.m,
                end = space.m,
                top = space.xl * 2,
                bottom = space.xl
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally){
            HeadTitle(text = "Welcome to Study Group!")
            Spacer(modifier = Modifier.height(space.l))
            HeadTitle(text = "Sign in")
            Spacer(modifier = Modifier.height(space.xl))
            TextInput(label = "Email")
            Spacer(modifier = Modifier.height(space.m))
            TextInput(label = "Password*")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                ){
                Link("Don't have an account? Sign up here!")
            }
        }
        AppButton(

            type = ButtonType.PRIMARY,
            text = "Sign In",
            modifier = Modifier.fillMaxWidth()
        )
    }
}



@Composable
fun Link(text: String){
    val colors = LocalCustomColors.current
    Text(
        text = text,
        color = colors.grey,
        textDecoration = TextDecoration.Underline
    )
}
@Composable
fun HeadTitle(text: String){
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewSignIn(){
    SignIn()
}