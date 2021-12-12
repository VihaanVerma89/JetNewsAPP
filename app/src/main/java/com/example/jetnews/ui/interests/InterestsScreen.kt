package com.example.jetnews.ui.interests

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetnews.R
import com.example.jetnews.data.interests.InterestsRepository
import com.example.jetnews.data.interests.TopicSelection
import com.example.jetnews.data.interests.TopicsMap
import com.example.jetnews.ui.components.InsetAwareTopAppBar
import com.example.jetnews.utils.produceUiState
import com.example.jetnews.utils.supportWideScreen
import com.google.accompanist.insets.navigationBarsPadding
import kotlinx.coroutines.launch


enum class Sections(val titleResId: Int) {
    Topics(R.string.interests_section_topics),
    People(R.string.interests_section_people),
    Publications(R.string.interests_section_publications)
}

class TabContent(val section: Sections, val content: @Composable () -> Unit)

@Composable
fun InterestsScreen(
    interestsRepository: InterestsRepository,
    openDrawer: () -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
) {

    val coroutineScope = rememberCoroutineScope()
    val topicsSection = TabContent(Sections.Topics) {

        val (topics) = produceUiState(interestsRepository) {
            getTopics()
        }
        val selectedTopics by interestsRepository.observeTopicsSelected()
            .collectAsState(initial = setOf())

        val onTopicSelect: (TopicSelection) -> Unit = {
            coroutineScope.launch { interestsRepository.toggleTopicSelection(it) }
        }
        val data = topics.value.data ?: return@TabContent
        TopicList(data, selectedTopics, onTopicSelect)
    }

    val peopleSection = TabContent(Sections.People) {
        val (people) = produceUiState(interestsRepository) {
            getPeople()
        }
        val selectedPeople by interestsRepository.observePeopleSelected().collectAsState(setOf())
        val onPeopleSelect: (String) -> Unit = {
            coroutineScope.launch { interestsRepository.togglePersonSelected(it) }
        }
        val data = people.value.data ?: return@TabContent
        PeopleList(data, selectedPeople, onPeopleSelect)
    }

    val publicationSection = TabContent(Sections.Publications) {
        val (publications) = produceUiState(interestsRepository) {
            getPublications()
        }
        val selectedPublications by interestsRepository.observePublicationSelected()
            .collectAsState(setOf())
        val onPublicationSelect: (String) -> Unit = {
            coroutineScope.launch { interestsRepository.togglePublicationSelected(it) }
        }
        val data = publications.value.data ?: return@TabContent
        PublicationList(data, selectedPublications, onPublicationSelect)
    }

    val tabContent = listOf(topicsSection, peopleSection, publicationSection)
    val (currentSection, updateSection) = rememberSaveable { mutableStateOf(tabContent.first().section) }
    InterestsScreen(
        tabContent = tabContent,
        tab = currentSection,
        onTabChange = updateSection,
        openDrawer = openDrawer,
        scaffoldState = scaffoldState
    )
}

@Composable
fun InterestsScreen(
    tabContent: List<TabContent>,
    tab: Sections,
    onTabChange: (Sections) -> Unit,
    openDrawer: () -> Unit,
    scaffoldState: ScaffoldState,
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            InsetAwareTopAppBar(
                title = { Text(stringResource(id = R.string.interests_title)) },
                navigationIcon = {
                    IconButton(onClick = openDrawer) {
                        Icon(
                            painter = painterResource(R.drawable.ic_jetnews_logo),
                            contentDescription = stringResource(R.string.cd_open_navigation_drawer)
                        )
                    }
                }
            )
        }
    ) {
        TabContent(tab, onTabChange, tabContent)
    }
}

@Composable
private fun TabContent(
    currentSection: Sections,
    updateSection: (Sections) -> Unit,
    tabContent: List<TabContent>
) {
    val selectedTabIndex = tabContent.indexOfFirst { it.section == currentSection }
    Column {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            backgroundColor = MaterialTheme.colors.onPrimary,
            contentColor = MaterialTheme.colors.primary,

            ) {
            tabContent.forEachIndexed { index, tabContent ->
                val colorText = if (selectedTabIndex == index) {
                    MaterialTheme.colors.primary
                } else {
                    MaterialTheme.colors.onSurface.copy(alpha = 0.8f)
                }
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = {
                        updateSection(tabContent.section)
                    },
                    modifier = Modifier
                        .heightIn(min = 48.dp)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = stringResource(id = tabContent.section.titleResId),
                        color = colorText,
                        style = MaterialTheme.typography.subtitle2,
                        modifier = Modifier.paddingFromBaseline(top = 20.dp)
                    )
                }
            }
        }
        Divider(
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .supportWideScreen()
        ) {
            // display the current tab content which is a @Composable () -> Unit
            tabContent[selectedTabIndex].content()
        }
    }
}

@Composable
fun TopicList(
    topics: TopicsMap,
    selectedTopics: Set<TopicSelection>,
    onTopicSelect: (TopicSelection) -> Unit,
) {
    TabWithSections(topics, selectedTopics, onTopicSelect)
}

@Composable
private fun PeopleList(
    people: List<String>,
    selectedPeople: Set<String>,
    onPersonSelect: (String) -> Unit
) {
    TabWithTopics(people, selectedPeople, onPersonSelect)
}

/**
 * Display a list for publications tab
 *
 * @param publications (state) publications to display
 * @param selectedPublications (state) currently selected publications
 * @param onPublicationSelect (event) request a publication selection be changed
 */
@Composable
private fun PublicationList(
    publications: List<String>,
    selectedPublications: Set<String>,
    onPublicationSelect: (String) -> Unit
) {
    TabWithTopics(publications, selectedPublications, onPublicationSelect)
}

/**
 * Display a simple list of topics
 *
 * @param topics (state) topics to display
 * @param selectedTopics (state) currently selected topics
 * @param onTopicSelect (event) request a topic selection be changed
 */
@Composable
private fun TabWithTopics(
    topics: List<String>,
    selectedTopics: Set<String>,
    onTopicSelect: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(top = 16.dp)
            .navigationBarsPadding()
    ) {
        items(topics) { topic ->
            TopicItem(
                topic,
                selected = selectedTopics.contains(topic)
            ) { onTopicSelect(topic) }
            TopicDivider()
        }
    }
}


@Composable
private fun TabWithSections(
    sections: TopicsMap,
    selectedTopics: Set<TopicSelection>,
    onTopicSelect: (TopicSelection) -> Unit
) {
    LazyColumn(Modifier.navigationBarsPadding()) {
        sections.forEach { (section, topics) ->
            item {
                Text(
                    text = section,
                    modifier = Modifier
                        .padding(16.dp)
                        .semantics { heading() },
                    style = MaterialTheme.typography.subtitle1
                )
            }
            items(topics) { topic ->
                TopicItem(
                    itemTitle = topic,
                    selected = selectedTopics.contains(TopicSelection(section, topic))
                ) { onTopicSelect(TopicSelection(section, topic)) }
                TopicDivider()
            }
        }
    }
}

@Composable
private fun TopicItem(itemTitle: String, selected: Boolean, onToggle: () -> Unit) {
    val image = painterResource(R.drawable.placeholder_1_1)
    Row(
        modifier = Modifier
            .toggleable(
                value = selected,
                onValueChange = { onToggle() }
            )
            .padding(horizontal = 16.dp)
    ) {
        Image(
            painter = image,
            contentDescription = null, // decorative
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .size(56.dp, 56.dp)
                .clip(RoundedCornerShape(4.dp))
        )
        Text(
            text = itemTitle,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(16.dp),
            style = MaterialTheme.typography.subtitle1
        )
        Spacer(Modifier.weight(1f))
        SelectTopicButton(
            modifier = Modifier.align(Alignment.CenterVertically),
            selected = selected
        )
    }
}

@Composable
fun TopicDivider(
) {
    Divider(
        modifier = Modifier.padding(start = 90.dp, top = 8.dp, bottom = 8.dp),
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f)
    )

}

@Preview
@Composable
fun PreviewInterest(
) {

    Column {
        TopicItem(itemTitle = "topic name", selected = false) {

        }
        TopicDivider()
    }
}
