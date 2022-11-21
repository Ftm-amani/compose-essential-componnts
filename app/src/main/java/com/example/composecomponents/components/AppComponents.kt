package com.example.composecomponents.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.*
import coil.compose.rememberAsyncImagePainter

@ExperimentalComposeUiApi
@Composable
fun CustomInputField(
	modifier: Modifier = Modifier,
	valueState: MutableState<String>,
	padding: Dp,
	isEnabled: Boolean,
	isSingleLine: Boolean,
	label: String,
	isNecessary: Boolean,
	keyboardType: KeyboardType,
	imeAction: ImeAction,
	onAction: KeyboardActions
) {
	Column(
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.Start
	) {
		
		Text(
			text = buildAnnotatedString {
				withStyle(
					style = SpanStyle(
					)
				) {
					append(label)
				}
				withStyle(
					style = SpanStyle(
						color = red,
						fontWeight = FontWeight.Bold
					)
				) {
					if (isNecessary) {
						append(" ✽")
					} else {
						append("")
					}
				}
			},
			modifier = modifier
				.padding(start = 10.dp, end = 10.dp),
			fontSize = 14.sp,
			fontWeight = FontWeight.Normal,
			color = darkGray,
			style = MaterialTheme.typography.body1,
		)
		
		OutlinedTextField(
			value = valueState.value,
			onValueChange = { valueState.value = it },
			shape = RoundedCornerShape(8.dp),
			modifier = Modifier
				.padding(
					bottom = 10.dp,
					start = padding
				)
				.fillMaxWidth()
				.height(56.dp),
			textStyle = TextStyle(fontSize = 18.sp),
			
			singleLine = isSingleLine,
			enabled = isEnabled,
			colors = TextFieldDefaults.outlinedTextFieldColors(
				cursorColor = colorPrimary,
				focusedBorderColor = colorPrimary,
				unfocusedBorderColor = darkGray
			),
			keyboardOptions = KeyboardOptions(
				keyboardType = keyboardType,
				imeAction = imeAction
			),
			keyboardActions = onAction
		
		)
	}
}

@Composable
fun CustomDropDown(
	modifier: Modifier,
	suggestions: List<String>,
	label: String,
	isNecessary: Boolean,
	selectedOption: MutableState<String>,
	requiredWidth: Dp,
	onRowClick: (String) -> Unit = {}
) {
	var expanded by remember { mutableStateOf(false) }
	var selectedText by remember { mutableStateOf(suggestions.first()) }
	var textFieldSize by remember { mutableStateOf(Size.Zero) }
	val iconSrc = if (expanded) R.drawable.arrow else R.drawable.ic_arrow_down
	Column(
		modifier = Modifier
			.width(requiredWidth)
			.fillMaxWidth()
	) {
		Text(
			text = buildAnnotatedString {
				withStyle(
					style = SpanStyle(
					)
				) {
					append(label)
				}
				withStyle(
					style = SpanStyle(
						color = red,
						fontWeight = FontWeight.Bold
					)
				) {
					if (isNecessary) {
						append(" ✽")
					} else {
						append("")
					}
				}
			},
			fontSize = 14.sp,
			fontWeight = FontWeight.Normal,
			color = darkGray,
			style = MaterialTheme.typography.body1,
		)
		OutlinedTextField(
			value = selectedText,
			onValueChange = {
				selectedText = it
				selectedOption.value = it
			},
			shape = RoundedCornerShape(8.dp),
			colors = TextFieldDefaults.outlinedTextFieldColors(
				cursorColor = colorPrimary,
				focusedBorderColor = colorPrimary,
				unfocusedBorderColor = darkGray
			),
			textStyle = TextStyle(fontSize = 18.sp),
			modifier = Modifier
				.clickable(enabled = false, onClick = {expanded = !expanded})
				.fillMaxWidth()
				.padding(bottom = 10.dp)
				.height(56.dp)
//				.clickable { expanded = !expanded }
				.onGloballyPositioned { coordinates ->
					//This value is used to assign to the DropDown the same width
					textFieldSize = coordinates.size.toSize()
				},
			trailingIcon = {
				Icon(
					painter = rememberAsyncImagePainter(model = iconSrc),
					contentDescription = "up",
					Modifier
						.clickable {
							expanded = !expanded
						}
						.height(25.dp)
						.width(25.dp)
				)
			},
		)
		DropdownMenu(
			expanded = expanded,
			onDismissRequest = {
				expanded = false
			},
			modifier = Modifier
				.width(with(LocalDensity.current) {
					textFieldSize.width.toDp()
				})
		) {
			suggestions.forEach { label ->
				DropdownMenuItem(onClick = {
					selectedText = label
					onRowClick.invoke(selectedText)
					expanded = !expanded
				}) {
					Text(text = label)
				}
			}
		}
	}
}

@Composable
private fun CustomTextField(
	modifier: Modifier = Modifier,
	valueState: MutableState<String>,
	leadingIcon: (@Composable () -> Unit)? = null,
	trailingIcon: (@Composable () -> Unit)? = null,
	placeholderText: String = "Placeholder",
	fontSize: TextUnit = MaterialTheme.typography.body2.fontSize
) {
	var text by rememberSaveable { mutableStateOf("") }
	
	BasicTextField(modifier = modifier
		.background(
			MaterialTheme.colors.surface,
			MaterialTheme.shapes.small,
		)
		.fillMaxWidth(),
				   value = valueState.value,
				   onValueChange = { valueState.value = it },
				   
				   singleLine = true,
				   cursorBrush = SolidColor(MaterialTheme.colors.primary),
				   textStyle = LocalTextStyle.current.copy(
					   color = MaterialTheme.colors.onSurface,
					   fontSize = fontSize
				   ),
				   decorationBox = { innerTextField ->
					   Row(
						   modifier,
						   verticalAlignment = Alignment.CenterVertically
					   ) {
						   if (leadingIcon != null) leadingIcon()
						   Box(Modifier.weight(1f)) {
							   if (text.isEmpty()) Text(
								   placeholderText,
								   style = LocalTextStyle.current.copy(
									   color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f),
									   fontSize = fontSize
								   )
							   )
							   innerTextField()
						   }
						   if (trailingIcon != null) trailingIcon()
					   }
				   }
	)
}

@Composable
fun Indicator(
	modifier : Modifier = Modifier,
	size: Dp = 32.dp, // indicator size
	sweepAngle: Float = 270f, // angle (lenght) of indicator arc
	color: Color = colorPrimary, // color of indicator arc line
	strokeWidth: Dp = ProgressIndicatorDefaults.StrokeWidth //width of cicle and ar lines
) {
	////// animation //////
	// docs recommend use transition animation for infinite loops
	// https://developer.android.com/jetpack/compose/animation
	val transition = rememberInfiniteTransition()
	
	// define the changing value from 0 to 360.
	// This is the angle of the beginning of indicator arc
	// this value will change over time from 0 to 360 and repeat indefinitely.
	// it changes starting position of the indicator arc and the animation is obtained
	val currentArcStartAngle by transition.animateValue(
		0,
		360,
		Int.VectorConverter,
		infiniteRepeatable(
			animation = tween(
				durationMillis = 900,
				easing = LinearEasing
			)
		)
	)
	
	////// draw /////
	
	// define stroke with given width and arc ends type considering device DPI
	val stroke = with(LocalDensity.current) {
		Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Square)
	}
	
	// draw on canvas
	Canvas(
		modifier
			.progressSemantics() // (optional) for Accessibility services
			.size(size) // canvas size
			.padding(strokeWidth / 2) //padding. otherwise, not the whole circle will fit in the canvas
	) {
		// draw "background" (gray) circle with defined stroke.
		// without explicit center and radius it fit canvas bounds
		drawCircle(Color.LightGray, style = stroke)
		
		// draw arc with the same stroke
		drawArc(
			color,
			// arc start angle
			// -90 shifts the start position towards the y-axis
			startAngle = currentArcStartAngle.toFloat() - 90,
			sweepAngle = sweepAngle,
			useCenter = false,
			style = stroke
		)
	}
}

@Composable
fun LoadingView(showLoading : MutableState<Boolean>) {
	if (showLoading.value) {
		Column(
			modifier = Modifier
				.padding(top = 30.dp)
				.fillMaxWidth()
				.fillMaxHeight()
				.background(
					color = Color.White.copy(alpha = 0.5f)
				),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Indicator()
		}
	}
}


//@Composable
//fun
//@ExperimentalComposeUiApi
//@Preview(showBackground = true)
//@Composable
//fun PrevMenu() {
//
//	Indicator()
//
//}
