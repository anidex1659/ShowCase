package com.example.showcase.ui.popup.Dialogs.Ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.showcase.R
import com.example.showcase.features.MetaData.data.database.DatabaseProvider
import com.example.showcase.features.MetaData.data.remote.TmdbNetworkModule
import com.example.showcase.features.MetaData.data.remote.TmdbRemoteDataSource
import com.example.showcase.features.MetaData.data.repository.Artwork.ArtworkDownloader
import com.example.showcase.features.Player.model.storage.ArtworkStorageManager
import com.example.showcase.ui.popup.Dialogs.Model.ArtworkItem
import com.example.showcase.ui.popup.Dialogs.ViewModel.EditArtworkViewModel
import com.example.showcase.ui.popup.Dialogs.ViewModel.EditArtworkViewModelFactory
import com.example.showcase.ui.theme.VMocha

//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EditArtworkDialog(
	seriesId: Long,
	onDismiss: () -> Unit
) {

	val context = LocalContext.current

	val dao =
		DatabaseProvider
			.getDatabase(context)
			.dao()

	val remote =
		TmdbRemoteDataSource(
			TmdbNetworkModule.tmdbApi
		)

	val artworkDownloader =
		ArtworkDownloader(
			context = context,
			storageManager = ArtworkStorageManager(),
			dao = dao
		)

	val viewModel: EditArtworkViewModel =
		viewModel(
			factory = EditArtworkViewModelFactory(
				dao = dao,
				remote = remote,
				artworkDownloader = artworkDownloader
			)
		)

	val state by viewModel.uiState.collectAsState()

	var selectedIndex by remember {
		mutableStateOf(0)
	}

	LaunchedEffect(seriesId) {
		viewModel.loadSeriesArtwork(seriesId)
	}

	Dialog(
		onDismissRequest = onDismiss
	) {

		Card(
			modifier = Modifier
				.fillMaxWidth()
				.fillMaxHeight(0.85f),

			colors = CardDefaults.cardColors(
				containerColor = VMocha.Crust
			)
		) {

			Column(
				modifier = Modifier.fillMaxSize()
			) {


				SecondaryTabRow(
					selectedTabIndex = selectedIndex,
					containerColor = VMocha.Crust,
					contentColor = VMocha.Text
				) {

					Tab(
						selected = selectedIndex == 0,

						onClick = {
							selectedIndex = 0

							viewModel.selectArtworkType(
								"poster"
							)
						},

						text = {
							Text("Posters")
						}
					)

					Tab(
						selected = selectedIndex == 1,

						onClick = {
							selectedIndex = 1
							viewModel.selectArtworkType(
								"backdrop"
							)
						},

						text = {
							Text("Backdrops")
						}
					)

					Tab(
						selected = selectedIndex == 2,

						onClick = {
							selectedIndex = 2

							viewModel.selectArtworkType(
								"logo"
							)
						},

						text = {
							Text("Logos")
						}
					)
				}


				when (selectedIndex) {

					0 -> {

						PosterTab(
							currentArtwork =
								state.currentArtworkUri,

							selectedArtwork =
								state.selectedArtworkUrl,

							results =
								state.artworkResults,

							isLoading =
								state.isLoading,

							onSelect = {
								viewModel.selectArtwork(it)
							},

							onSave = {
								viewModel.saveArtwork()
							}
						)
					}


					1 -> {

						BackdropTab(
							currentArtwork =
								state.currentArtworkUri,

							selectedArtwork =
								state.selectedArtworkUrl,

							results =
								state.artworkResults,

							isLoading =
								state.isLoading,

							onSelect = {
								viewModel.selectArtwork(it)
							},

							onSave = {
								viewModel.saveArtwork()
							}
						)
					}


					2 -> {

						LogoTab(
							currentArtwork =
								state.currentArtworkUri,

							selectedArtwork =
								state.selectedArtworkUrl,

							results =
								state.artworkResults,

							isLoading =
								state.isLoading,

							onSelect = {
								viewModel.selectArtwork(it)
							},

							onSave = {
								viewModel.saveArtwork()
							}
						)
					}
				}
			}
		}
	}
}



@Composable
private fun ArtworkTab(
	currentArtwork: String?,
	selectedArtwork: String?,
	results: List<ArtworkItem>,
	isLoading: Boolean,
	onSelect: (String) -> Unit,
	onSave: () -> Unit
) {

	val preview =
		selectedArtwork
			?: currentArtwork

	Column(
		modifier = Modifier.fillMaxSize()
	) {

		AsyncImage(
			model = preview,
			contentDescription = null,
			contentScale = ContentScale.Fit,
			modifier = Modifier
				.fillMaxWidth()
				.height(250.dp)
				.padding(10.dp)
		)


		Button(
			onClick = onSave,

			enabled =
				selectedArtwork != null &&
						!isLoading,

			modifier = Modifier
				.align(Alignment.CenterHorizontally)
				.padding(bottom = 8.dp)
		) {

			Icon(
				imageVector = Icons.Default.Save,
				contentDescription = null
			)

			Spacer(
				Modifier.width(8.dp)
			)

			Text("Save Artwork")
		}


		HorizontalDivider(
			modifier = Modifier.padding(
				horizontal = 10.dp
			),

			color =
				VMocha.Text.copy(
					alpha = 0.8f
				)
		)


		if (isLoading) {

			Box(
				modifier = Modifier.fillMaxSize(),

				contentAlignment =
					Alignment.Center
			) {

				CircularProgressIndicator()
			}

		} else {

			LazyVerticalGrid(
				columns = GridCells.Fixed(3),

				modifier =
					Modifier.fillMaxSize()
			) {

				items(results) { artwork ->

					AsyncImage(

						model =
							artwork.url,

						contentDescription =
							null,

						contentScale =
							ContentScale.Crop,

						modifier =
							Modifier
								.aspectRatio(0.8f)
								.padding(3.dp)
								.clickable {

									onSelect(
										artwork.url
									)
								}
					)
				}
			}
		}
	}
}


@Composable
private fun PosterTab(
	currentArtwork: String?,
	selectedArtwork: String?,
	results: List<ArtworkItem>,
	isLoading: Boolean,
	onSelect: (String) -> Unit,
	onSave: () -> Unit
) {

	val preview =
		selectedArtwork ?: currentArtwork

	Column(
		modifier = Modifier.fillMaxSize()
	) {


		AsyncImage(
			model = preview,
			contentDescription = "Current poster",

			contentScale = ContentScale.Fit,

			modifier = Modifier
				.fillMaxWidth()
				.height(250.dp)
				.padding(10.dp)
		)

		Button(
			onClick = onSave,

			enabled =
				selectedArtwork != null &&
						!isLoading,

			modifier =
				Modifier
					.align(Alignment.CenterHorizontally)
					.padding(bottom = 8.dp)
		) {

			Icon(
				imageVector = Icons.Default.Save,
				contentDescription = null
			)

			Spacer(
				modifier = Modifier.width(8.dp)
			)

			Text("Save Poster")
		}


		HorizontalDivider(
			modifier = Modifier.padding(
				horizontal = 10.dp
			),

			color =
				VMocha.Text.copy(
					alpha = 0.8f
				)
		)



		if (isLoading) {

			Box(
				modifier = Modifier.fillMaxSize(),

				contentAlignment =
					Alignment.Center
			) {

				CircularProgressIndicator()
			}

		} else {

			LazyVerticalGrid(

				columns =
					GridCells.Fixed(3),

				modifier =
					Modifier.fillMaxSize()
			) {

				items(results) { artwork ->

					AsyncImage(

						model =
							artwork.url,

						contentDescription =
							"Poster",

						contentScale =
							ContentScale.Crop,

						modifier =
							Modifier
								.aspectRatio(0.8f)
								.padding(3.dp)
								.clickable {

									onSelect(
										artwork.url
									)
								}
					)
				}
			}
		}
	}
}

@Composable
private fun BackdropTab(
	currentArtwork: String?,
	selectedArtwork: String?,
	results: List<ArtworkItem>,
	isLoading: Boolean,
	onSelect: (String) -> Unit,
	onSave: () -> Unit
) {

	val preview =
		selectedArtwork ?: currentArtwork

	Column(
		modifier = Modifier.fillMaxSize()
	) {


		AsyncImage(
			model = preview,
			contentDescription = "Current backdrop",

			contentScale =
				ContentScale.Crop,

			modifier = Modifier
				.fillMaxWidth()
				.height(200.dp)
				.padding(10.dp)
		)




		Button(
			onClick = onSave,

			enabled =
				selectedArtwork != null &&
						!isLoading,

			modifier =
				Modifier
					.align(Alignment.CenterHorizontally)
					.padding(bottom = 8.dp)
		) {

			Icon(
				imageVector = Icons.Default.Save,
				contentDescription = null
			)

			Spacer(
				modifier = Modifier.width(8.dp)
			)

			Text("Save Backdrop")
		}


		HorizontalDivider(
			modifier = Modifier.padding(
				horizontal = 10.dp
			),

			color =
				VMocha.Text.copy(
					alpha = 0.8f
				)
		)



		if (isLoading) {

			Box(
				modifier =
					Modifier.fillMaxSize(),

				contentAlignment =
					Alignment.Center
			) {

				CircularProgressIndicator()
			}

		} else {

			LazyVerticalGrid(

				columns =
					GridCells.Fixed(2),

				modifier =
					Modifier.fillMaxSize()
			) {

				items(results) { artwork ->

					AsyncImage(

						model =
							artwork.url,

						contentDescription =
							"Backdrop",

						contentScale =
							ContentScale.Crop,

						modifier =
							Modifier
								.aspectRatio(2f)
								.padding(3.dp)
								.clickable {

									onSelect(
										artwork.url
									)
								}
					)
				}
			}
		}
	}
}
@Composable
private fun LogoTab(
	currentArtwork: String?,
	selectedArtwork: String?,
	results: List<ArtworkItem>,
	isLoading: Boolean,
	onSelect: (String) -> Unit,
	onSave: () -> Unit
) {

	val preview =
		selectedArtwork ?: currentArtwork

	Column(
		modifier = Modifier.fillMaxSize()
	) {



		AsyncImage(
			model = preview,
			contentDescription = "Current logo",

			contentScale =
				ContentScale.Fit,

			modifier = Modifier
				.fillMaxWidth()
				.height(200.dp)
				.padding(10.dp)
		)



		Button(
			onClick = onSave,

			enabled =
				selectedArtwork != null &&
						!isLoading,

			modifier =
				Modifier
					.align(Alignment.CenterHorizontally)
					.padding(bottom = 8.dp)
		) {

			Icon(
				imageVector =
					Icons.Default.Save,

				contentDescription =
					null
			)

			Spacer(
				modifier =
					Modifier.width(8.dp)
			)

			Text(
				"Save Logo"
			)
		}


		HorizontalDivider(
			modifier =
				Modifier.padding(
					horizontal = 10.dp
				),

			color =
				VMocha.Text.copy(
					alpha = 0.8f
				)
		)

		if (isLoading) {

			Box(
				modifier =
					Modifier.fillMaxSize(),

				contentAlignment =
					Alignment.Center
			) {

				CircularProgressIndicator()
			}

		} else {

			LazyVerticalGrid(

				columns =
					GridCells.Fixed(2),

				modifier =
					Modifier.fillMaxSize()
			) {

				items(results) { artwork ->

					AsyncImage(

						model =
							artwork.url,

						contentDescription =
							"Logo",

						contentScale =
							ContentScale.Fit,

						modifier =
							Modifier
								.aspectRatio(2f)
								.padding(3.dp)
								.clickable {

									onSelect(
										artwork.url
									)
								}
					)
				}
			}
		}
	}
}

