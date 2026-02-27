package com.example.ethelterrycurrencyconverter.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ethelterrycurrencyconverter.CurrencyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: CurrencyViewModel) {
    val currencies by viewModel.availableCurrencies
    val isLoading by viewModel.isLoading
    val backgroundColor by viewModel.backgroundColor
    val language by viewModel.language
    val amount by viewModel.amount
    val fromCurrency by viewModel.fromCurrency
    val toCurrency by viewModel.toCurrency
    val result by viewModel.conversionResult

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = viewModel.translate("title"),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { viewModel.setAmount(it) },
            label = { Text(viewModel.translate("amount")) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = viewModel.translate("from"), fontWeight = FontWeight.SemiBold)
        GenericDropdown(currencies, fromCurrency) { viewModel.setFromCurrency(it) }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(text = viewModel.translate("to"), fontWeight = FontWeight.SemiBold)
        GenericDropdown(currencies, toCurrency) { viewModel.setToCurrency(it) }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.calculateConversion() },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(viewModel.translate("convert"), fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (result.isNotEmpty()) {
            Text(
                text = "${viewModel.translate("result")} $result",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
        
        Settings(viewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericDropdown(options: List<String>, selectedOption: String, onOptionSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded, 
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun Settings(viewModel: CurrencyViewModel) {
    val language by viewModel.language
    val isMusicEnabled by viewModel.isMusicEnabled
    val supportedLanguages = viewModel.supportedLanguages

    Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = viewModel.translate("settings"),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = viewModel.translate("bg_color"), fontWeight = FontWeight.Medium)
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = viewModel.backgroundColor.value == Color.White, onClick = { viewModel.setBackgroundColor(Color.White) })
            Text(viewModel.translate("white"))
            RadioButton(selected = viewModel.backgroundColor.value == Color.LightGray, onClick = { viewModel.setBackgroundColor(Color.LightGray) })
            Text(viewModel.translate("gray"))
        }
        
        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = viewModel.translate("music"), fontWeight = FontWeight.Medium)
            Checkbox(checked = isMusicEnabled, onCheckedChange = { viewModel.toggleMusic() })
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = viewModel.translate("language"), fontWeight = FontWeight.Medium)
        GenericDropdown(supportedLanguages, language) { viewModel.setLanguage(it) }
    }
}
