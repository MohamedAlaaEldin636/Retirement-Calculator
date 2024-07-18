package my.ym.retirementcalculator.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.microsoft.appcenter.Flags
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.analytics.EventProperties

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
            onValueChange = { monthlySalary = it }
        )

        var interestRate by rememberSaveable { mutableStateOf("") }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Interest Rate") },
            value = interestRate,
            onValueChange = { interestRate = it }
        )

        var yourCurrentAge by rememberSaveable { mutableStateOf("") }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Your Current Age") },
            value = yourCurrentAge,
            onValueChange = { yourCurrentAge = it }
        )

        var plannedRetirementAge by rememberSaveable { mutableStateOf("") }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Planned Retirement Age") },
            value = plannedRetirementAge,
            onValueChange = { plannedRetirementAge = it }
        )

        var currentSavings by rememberSaveable { mutableStateOf("") }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Current Savings") },
            value = currentSavings,
            onValueChange = { currentSavings = it }
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

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                val properties = EventProperties()
                    .set("value 1", 10)
                    .set("value 2", 20)
                    .set("value 3", 30)

                Analytics.trackEvent(
                    "Clicked button of Calculate with props and critical isa",
                    properties,
                    Flags.CRITICAL
                )

                showToast("Clicked el7")

                //throw RuntimeException("Trial 1")
                //Crashes.generateTestCrash()
            },
        ) {
            Text(text = "Calculate")
        }

        Text(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            text = "At this rate, with your current monthly savings you will have 1,000,000\$ by 65 isa.",
            textAlign = TextAlign.Center,
        )
    }
}
