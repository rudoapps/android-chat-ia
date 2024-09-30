package es.rudo.chatia.presentation.chat

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import es.rudo.chatia.App
import es.rudo.chatia.R
import es.rudo.chatia.core.network.ErrorHandlerImpl
import es.rudo.chatia.core.utils.toColorFromHex
import es.rudo.chatia.data.datasource.chat.remote.ChatRemoteDataSourceImpl
import es.rudo.chatia.data.repository.ChatRepositoryImpl
import es.rudo.chatia.di.NetworkMoshiMoshi
import es.rudo.chatia.domain.entity.ConfigChat
import es.rudo.chatia.domain.entity.Message
import es.rudo.chatia.domain.usecase.chat.ChatUseCaseImpl
import es.rudo.chatia.presentation.components.custom.CustomTextField
import es.rudo.chatia.ui.theme.ChatIATheme
import es.rudo.chatia.ui.theme.Dimens
import es.rudo.chatia.ui.theme.backgroundBlue
import es.rudo.chatia.ui.theme.backgroundGray
import es.rudo.chatia.ui.theme.backgroundLightBlue
import es.rudo.chatia.ui.theme.backgroundLightRed
import es.rudo.chatia.ui.theme.borderGray
import es.rudo.chatia.ui.theme.letterDarkBlue
import es.rudo.chatia.ui.theme.lightGray

object MoshiMoshiInstance {
    private val context = App.instance
    val networkMoshiMoshi = NetworkMoshiMoshi(context)
}

private val chatDataSource = ChatRemoteDataSourceImpl(MoshiMoshiInstance.networkMoshiMoshi)
private val chatRepository = ChatRepositoryImpl(chatDataSource, ErrorHandlerImpl())
private val chatUseCase = ChatUseCaseImpl(chatRepository)

const val LOW_ALPHA = 0.4f

@Composable
fun ChatAI(
    customerId: Int,
    viewModel: ChatViewModel = ChatViewModel(chatUseCase),
    navigateBack: () -> Unit
) {
    BackHandler {
        navigateBack()
    }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        viewModel.setConfig(customerId)
    }
    ChatIATheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.White,
            topBar = {
                TopBar(
                    navigateBack = navigateBack
                )
            },
            bottomBar = {
                BottomBar(
                    remember {
                        {
                            viewModel.sendMessage(it)
                        }
                    },
                    config = uiState.config,
                    isConfigLoaded = uiState.isConfigLoaded
                )
            }
        )
        {
            Chat(
                modifier = Modifier
                    .padding(top = Dimens.padding24)
                    .fillMaxWidth()
                    .padding(it),
                messages = uiState.messages,
                isLoading = uiState.isMessageLoading,
                hasGeneralError = uiState.hasGeneralError,
                hasConnectionError = uiState.hasConnectionError,
                messagesErrorIndex = uiState.indexMessagesError,
                config = uiState.config,
                resendMessage = remember { { viewModel.resendMessage() } }
            )
        }
    }
}

@Composable
fun Chat(
    modifier: Modifier,
    messages: List<Message>,
    isLoading: Boolean,
    hasGeneralError: Boolean,
    hasConnectionError: Boolean,
    messagesErrorIndex: List<Int>,
    config: ConfigChat?,
    resendMessage: () -> Unit
) {
    val chatListState = rememberLazyListState()

    LaunchedEffect(messages) {
        chatListState.animateScrollToItem(chatListState.layoutInfo.totalItemsCount)
    }

    Column(modifier = modifier) {
        LazyColumn(
            modifier = Modifier,
            state = chatListState
        )
        {
            items(messages) { message ->
                val isMessageError = messagesErrorIndex.contains(messages.indexOf(message))
                MessageComponent(
                    message = message,
                    isMessageError = isMessageError,
                    config = config
                )
            }
            item {
                AnimatedVisibility(isLoading) {
                    LoaderMessageComponent(
                        config?.colorOne?.toColorFromHex()?.copy(alpha = LOW_ALPHA) ?: backgroundLightBlue
                    )
                }
                AnimatedVisibility(hasGeneralError) {
                    GeneralErrorComponent(resendMessage)
                }
                AnimatedVisibility(hasConnectionError) {
                    ConnectionErrorComponent(resendMessage)
                }
            }
        }
    }
}

@Composable
fun LoaderMessageComponent(backgroundTextColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = Dimens.padding16, bottom = Dimens.padding12),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            modifier = Modifier
                .padding(end = Dimens.padding56)
                .background(
                    color = backgroundTextColor,
                    shape = RoundedCornerShape(Dimens.roundedCorner24)
                )
                .padding(Dimens.padding12),
            textAlign = TextAlign.Start,
            color = letterDarkBlue.copy(alpha = LOW_ALPHA),
            text = stringResource(id = R.string.writing)
        )
    }
}

@Composable
fun MessageComponent(message: Message, isMessageError: Boolean, config: ConfigChat?) {
    if (message.isResponse) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = Dimens.padding16,
                    bottom = Dimens.padding12,
                    end = Dimens.padding56
                ),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                modifier = Modifier
                    .background(
                        config?.colorOne
                            ?.toColorFromHex()
                            ?.copy(alpha = LOW_ALPHA) ?: backgroundLightBlue,
                        shape = RoundedCornerShape(Dimens.roundedCorner24)
                    )
                    .padding(Dimens.padding12),
                textAlign = TextAlign.Start,
                text = message.content
            )
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    end = Dimens.padding16,
                    bottom = Dimens.padding12,
                    start = Dimens.padding56
                ),
            horizontalArrangement = Arrangement.End
        ) {
            Row(
                modifier = Modifier
                    .background(
                        if (isMessageError) backgroundGray else {
                            lightGray
                        },
                        shape = RoundedCornerShape(Dimens.roundedCorner24)
                    )
            ) {
                Text(
                    modifier = Modifier
                        .padding(Dimens.padding12)
                        .weight(1f, fill = false),
                    text = message.content
                )
                if (isMessageError) {
                    Icon(
                        modifier = Modifier
                            .padding(
                                bottom = Dimens.padding16,
                                end = Dimens.padding12
                            )
                            .align(Alignment.Bottom)
                            .size(Dimens.size15),
                        painter = painterResource(id = R.drawable.ic_alert),
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
fun TopBar(
    navigateBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .height(Dimens.height54)
            .fillMaxWidth()
            .drawBehind {
                drawLine(
                    color = borderGray,
                    strokeWidth = Dimens.thickness1.toPx(),
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height)
                )
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        ConstraintLayout {
            val (icon, text) = createRefs()
            IconButton(
                modifier = Modifier
                    .constrainAs(icon) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(start = Dimens.padding16)
                    .size(Dimens.size24),
                onClick = { navigateBack() }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_left_arrow),
                    contentDescription = null
                )
            }
            Text(
                modifier = Modifier
                    .constrainAs(text) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .fillMaxWidth(),
                fontSize = Dimens.fontSize16,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                text = stringResource(id = R.string.help)
            )
        }
    }
}

@Composable
fun BottomBar(
    chatMessages: (String) -> Unit,
    config: ConfigChat?,
    isConfigLoaded: Boolean
) {
    if (isConfigLoaded) {
        val inputMessage = remember { mutableStateOf("") }
        Row(
            modifier = Modifier
                .imePadding()
                .drawBehind {
                    drawLine(
                        color = borderGray,
                        strokeWidth = Dimens.thickness1.toPx(),
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f)
                    )
                }
                .padding(top = Dimens.padding12, start = Dimens.padding12, end = Dimens.padding12)
                .windowInsetsPadding(WindowInsets(bottom = Dimens.padding12))
                .fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
        ) {
            CustomTextField(
                modifier = Modifier
                    .weight(1f),
                backGroundColor = lightGray,
                label = stringResource(id = R.string.write_here),
                text = inputMessage
            )
            Spacer(modifier = Modifier.width(Dimens.width8))
            Button(
                modifier = Modifier
                    .size(Dimens.size54),
                shape = RoundedCornerShape(Dimens.roundedCorner18),
                colors = ButtonDefaults.buttonColors(
                    containerColor = config?.colorOne?.toColorFromHex() ?: backgroundBlue,
                    contentColor = config?.colorTwo?.toColorFromHex() ?: backgroundLightBlue,
                    disabledContainerColor = config?.colorOne?.toColorFromHex()?.copy(alpha = LOW_ALPHA)
                        ?: backgroundLightBlue,
                    disabledContentColor = config?.colorTwo?.toColorFromHex()?.copy(alpha = LOW_ALPHA)
                        ?: lightGray
                ),
                enabled = inputMessage.value.isNotBlank(),
                contentPadding = PaddingValues(0.dp),
                onClick = {
                    chatMessages(inputMessage.value)
                    inputMessage.value = ""
                }
            ) {
                Icon(
                    modifier = Modifier
                        .size(Dimens.size22),
                    painter = painterResource(id = R.drawable.ic_send),
                    contentDescription = null
                )
            }
        }
    }

}

@Composable
fun GeneralErrorComponent(resendMessage: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.padding16)
            .padding(bottom = Dimens.padding12),
        horizontalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.background(
                backgroundLightRed,
                shape = RoundedCornerShape(Dimens.roundedCorner12)
            )
        ) {
            Text(
                modifier = Modifier
                    .padding(
                        top = Dimens.padding12,
                        bottom = Dimens.padding12,
                        start = Dimens.padding12
                    )
                    .weight(1f, fill = false),
                text = stringResource(id = R.string.error_sending_message)
            )
            Text(
                modifier = Modifier
                    .padding(
                        start = Dimens.padding4,
                        top = Dimens.padding12,
                        bottom = Dimens.padding12,
                        end = Dimens.padding12
                    )
                    .align(Alignment.CenterVertically)
                    .clickable {
                        resendMessage()
                    },
                text = stringResource(id = R.string.try_again),
                textDecoration = TextDecoration.Underline
            )
        }
    }
}

@Composable
fun ConnectionErrorComponent(resendMessage: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.padding16)
            .padding(bottom = Dimens.padding12),
        horizontalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .background(
                    color = backgroundLightRed,
                    shape = RoundedCornerShape(Dimens.roundedCorner12)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .padding(
                        top = Dimens.padding12,
                        bottom = Dimens.padding12,
                        start = Dimens.padding12
                    )
                    .size(Dimens.size15),
                painter = painterResource(id = R.drawable.ic_alert),
                contentDescription = null
            )
            Text(
                modifier = Modifier
                    .padding(
                        top = Dimens.padding8,
                        bottom = Dimens.padding8,
                        start = Dimens.padding8,
                    ),
                text = stringResource(id = R.string.phone_whitout_connection)
            )
            Text(
                modifier = Modifier
                    .padding(
                        start = Dimens.padding4,
                        top = Dimens.padding12,
                        bottom = Dimens.padding12,
                        end = Dimens.padding12
                    )
                    .align(Alignment.CenterVertically)
                    .clickable {
                        resendMessage()
                    },
                text = stringResource(id = R.string.try_again),
                textDecoration = TextDecoration.Underline
            )
        }
    }
}