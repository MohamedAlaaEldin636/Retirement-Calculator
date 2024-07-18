package my.ym.retirementcalculator.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.microsoft.appcenter.Flags
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.analytics.EventProperties
import my.ym.retirementcalculator.extensions.orZero
import kotlin.math.pow

@Composable
fun ScreenHome() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        var monthlySalary by rememberSaveable { mutableStateOf("") }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Monthly Salary") },
            value = monthlySalary,
            onValueChange = { monthlySalary = it },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            )
        )

        var interestRate by rememberSaveable { mutableStateOf("") }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Interest Rate") },
            value = interestRate,
            onValueChange = { interestRate = it },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            )
        )

        var yourCurrentAge by rememberSaveable { mutableStateOf("") }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Your Current Age") },
            value = yourCurrentAge,
            onValueChange = { yourCurrentAge = it },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )

        var plannedRetirementAge by rememberSaveable { mutableStateOf("") }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Planned Retirement Age") },
            value = plannedRetirementAge,
            onValueChange = { plannedRetirementAge = it },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )

        var currentSavings by rememberSaveable { mutableStateOf("") }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Current Savings") },
            value = currentSavings,
            onValueChange = { currentSavings = it },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            )
        )

        var toast: Toast? by remember { mutableStateOf(null) }
        val context = LocalContext.current
        val cancelToast: () -> Unit = {
            toast?.cancel()
            toast = null
        }
        val showToast: (String) -> Unit = {
            cancelToast()
            toast = Toast.makeText(context, it, Toast.LENGTH_SHORT)
            toast?.show()
        }

        var textResult by rememberSaveable { mutableStateOf("") }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                val properties = EventProperties()
                    .set("Monthly Salary", monthlySalary)
                    .set("Interest Rate", interestRate)
                    .set("Your Current Salary", yourCurrentAge)
                    .set("Planned Retirement Age", plannedRetirementAge)
                    .set("Current Savings", currentSavings)

                when {
                    monthlySalary.toFloatOrNull().orZero() <= 0f -> {
                        Analytics.trackEvent("Wrong monthly salary", properties, Flags.CRITICAL)

                        showToast("Incorrect monthly salary")

                        textResult = ""
                    }
                    interestRate.toFloatOrNull().orZero() <= 0f -> {
                        Analytics.trackEvent("Wrong interest rate", properties, Flags.CRITICAL)

                        showToast("Incorrect interest rate")

                        textResult = ""
                    }
                    yourCurrentAge.toIntOrNull().orZero() !in 13..200 -> {
                        Analytics.trackEvent("Wrong current age", properties, Flags.CRITICAL)

                        showToast("Incorrect current age")

                        textResult = ""
                    }
                    plannedRetirementAge.toIntOrNull().orZero() !in 13..200
                            || plannedRetirementAge.toIntOrNull().orZero() <= yourCurrentAge.toIntOrNull().orZero() -> {
                        Analytics.trackEvent("Wrong planned retirement age", properties, Flags.CRITICAL)

                        showToast("Incorrect planned retirement age")

                        textResult = ""
                    }
                    else -> {
                        Analytics.trackEvent(
                            "Successful Calculate Click ( Screen Home )",
                            properties,
                            Flags.DEFAULTS
                        )

                        val futureSavings = calculateRetirement(
                            interestRate.toFloatOrNull().orZero(),
                            currentSavings.toFloatOrNull().orZero(),
                            monthlySalary.toFloatOrNull().orZero(),
                            (plannedRetirementAge.toIntOrNull().orZero() - yourCurrentAge.toIntOrNull().orZero()) * 12
                        )

                        textResult = "At the current rate of $interestRate%," +
                                "saving $monthlySalary\$ a month you will have $futureSavings\$ by $plannedRetirementAge"
                    }
                }
            },
        ) {
            Text(text = "Calculate")
        }

        Text(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            text = textResult,
            textAlign = TextAlign.Center,
        )
    }
}

private fun calculateRetirement(interestRate: Float, currentSavings: Float, monthly: Float, numMonths: Int): Float {
    var futureSavings = currentSavings * (1+(interestRate/100/12)).pow(numMonths)

    for (i in 1..numMonths) {
        futureSavings += monthly * (1+(interestRate/100/12)).pow(i)
    }

    return  futureSavings
}
