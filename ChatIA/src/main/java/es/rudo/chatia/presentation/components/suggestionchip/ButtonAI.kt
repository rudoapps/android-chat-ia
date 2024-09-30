package es.rudo.chatia.presentation.components.suggestionchip

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ChipColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import es.rudo.chatia.ui.theme.backgroundBlue
import es.rudo.chatia.ui.theme.backgroundLightBlue
import es.rudo.chatia.R
import es.rudo.chatia.core.network.ErrorHandlerImpl
import es.rudo.chatia.core.utils.toColorFromHex
import es.rudo.chatia.data.datasource.chat.remote.ChatRemoteDataSourceImpl
import es.rudo.chatia.data.repository.ChatRepositoryImpl
import es.rudo.chatia.domain.entity.ConfigChat
import es.rudo.chatia.domain.usecase.chat.ChatUseCaseImpl
import es.rudo.chatia.presentation.chat.MoshiMoshiInstance
import es.rudo.chatia.ui.theme.ChatIATheme
import es.rudo.chatia.ui.theme.Dimens
import es.rudo.chatia.ui.theme.backgroundDarkGray

private val chatDataSource = ChatRemoteDataSourceImpl(MoshiMoshiInstance.networkMoshiMoshi)
private val chatRepository = ChatRepositoryImpl(chatDataSource, ErrorHandlerImpl())
private val chatUseCase = ChatUseCaseImpl(chatRepository)

const val LOW_ALPHA = 0.4f

@Composable
fun ButtonAI(
    modifier: Modifier = Modifier,
    viewModel: ButtonAIViewModel = ButtonAIViewModel(chatUseCase),
    navigateToChat: (Int) -> Unit,
    customerId: Int
) {
    LaunchedEffect(true) {
        viewModel.getConfig(customerId)
    }

    val isTooltipShowed = remember { mutableStateOf(true) }
    val uiState by viewModel.uiState.collectAsState()
    ChatIATheme {
        AnimatedVisibility(uiState.isLoaded) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = Dimens.padding12),
                horizontalArrangement = Arrangement.End
            ) {
                if (isTooltipShowed.value) {
                    TooltipButton(isShowed = isTooltipShowed, config = uiState.config)
                }
                ButtonChat(config = uiState.config, navigateToChat = navigateToChat, customerId = customerId)
            }
        }
    }
}

@Composable
private fun ButtonChat(
    config: ConfigChat? = null,
    customerId: Int,
    navigateToChat: (Int) -> Unit) {
    IconButton(
        onClick = {
            navigateToChat(customerId)
        },
        colors = IconButtonColors(
            containerColor = config?.colorOne?.toColorFromHex() ?: backgroundBlue,
            contentColor = White,
            disabledContentColor = backgroundBlue,
            disabledContainerColor = backgroundBlue
        )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.icon_chat),
            contentDescription = null,
            tint = config?.colorTwo?.toColorFromHex() ?: White,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_icon_button_chat))
        )
    }
}

@Composable
private fun TooltipButton(isShowed: MutableState<Boolean>, config: ConfigChat? = null) {
    SuggestionChip(
        modifier = Modifier.padding(end = dimensionResource(id = R.dimen.margin_tooltip)),
        onClick = {},
        enabled = false,
        colors = ChipColors(
            containerColor = Unspecified,
            labelColor = Black,
            leadingIconContentColor = config?.colorOne?.toColorFromHex() ?: Unspecified,
            trailingIconContentColor = White,
            disabledContainerColor = config?.colorOne?.toColorFromHex()?.copy(alpha = LOW_ALPHA) ?: backgroundLightBlue,
            disabledLabelColor = Black,
            disabledLeadingIconContentColor = backgroundDarkGray.copy(alpha = LOW_ALPHA),
            disabledTrailingIconContentColor = Unspecified
        ),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.round_corner_tooltip_chat)),
        label = {
            Text(
                text = config?.firstMessage ?: stringResource(id = R.string.tooltip_button_text),
                modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_text_tooltip))
            )
        },
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.icon_close),
                contentDescription = null,
                modifier = Modifier
                    .size(AssistChipDefaults.IconSize)
                    .clickable(enabled = true) {
                        isShowed.value = false
                    }
            )
        }
    )
}

@Preview
@Composable
fun PreviewButtonAI() {
    ButtonAI(customerId = 1, navigateToChat = {})
}

@Preview
@Composable
fun PreviewTooltip() {
    TooltipButton(remember { mutableStateOf(true) })
}

@Preview
@Composable
fun PreviewButtonChat() {
    ButtonChat(config = null, customerId = 1, navigateToChat = {})
}