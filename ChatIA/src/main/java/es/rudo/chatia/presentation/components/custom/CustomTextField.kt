package es.rudo.chatia.presentation.components.custom

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import es.rudo.chatia.ui.theme.Dimens
import es.rudo.chatia.ui.theme.backgroundLightBlue
import es.rudo.chatia.ui.theme.lightGray

private const val MAX_LINES = 4
@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    backGroundColor: Color? = null,
    label: String,
    text: MutableState<String>
) {
    OutlinedTextField(
        modifier = modifier,
        value = text.value,
        onValueChange = {
            text.value = it
        },
        placeholder = { Text(text = label) },
        singleLine = false,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor =  Color.Transparent,
            unfocusedBorderColor =  Color.Transparent,
            focusedContainerColor = backGroundColor ?:backgroundLightBlue,
            unfocusedContainerColor = backGroundColor ?:backgroundLightBlue,
            focusedLabelColor = Color.Unspecified,
            cursorColor = lightGray,
            selectionColors = TextSelectionColors(
                handleColor = Color.Unspecified,
                backgroundColor = backGroundColor ?:backgroundLightBlue
            )
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType =  KeyboardType.Text
        ),
        visualTransformation =
            VisualTransformation.None
        ,
        maxLines = MAX_LINES,
        shape = RoundedCornerShape(Dimens.roundedCorner18),
        trailingIcon = null
    )
}

@Composable
@Preview(showBackground = true)
fun CustomTextFieldPreview() {
    CustomTextField(
        label = "Escribe aquí",
        text = remember { mutableStateOf("") }
    )
}