import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.movieapp.model.Movie

@SuppressLint("DefaultLocale")
@Composable
fun MovieDetailScreen(title: String?, releaseDate: String?, overview: String?, posterPath: String?, popularity: Float) {
    // Scrollable column
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(30.dp)
            .verticalScroll(scrollState), // Make the column scrollable
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        posterPath?.let {
            val imageUrl = "https://image.tmdb.org/t/p/original$it"
            Image(
                painter = rememberImagePainter(imageUrl),
                contentDescription = "Movie Poster",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .padding(bottom = 16.dp)
            )
        }

        Text(
            text = title ?: "Unknown Title",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

        DetailItem(label = "📅 Release Date", value = releaseDate ?: "N/A")
        DetailItem(label = "📝 Overview", value = overview ?: "No description available.")
        DetailItem(label = "⭐ Popularity", value = "${String.format("%.1f", popularity)} / 10")

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color.Gray
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(start = 4.dp, top = 2.dp)
        )
    }
}
