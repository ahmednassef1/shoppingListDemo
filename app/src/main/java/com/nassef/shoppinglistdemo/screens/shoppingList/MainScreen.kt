package com.nassef.shoppinglistdemo.screens.shoppingList

//import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.nassef.domain.entities.Category
import com.nassef.domain.entities.Item
import com.nassef.shoppinglistdemo.R
import com.nassef.shoppinglistdemo.component.ScreenWithLoadingDialog
import com.nassef.shoppinglistdemo.ui.theme.LightBlue
import com.nassef.shoppinglistdemo.ui.theme.TransParentGray
import com.nassef.shoppinglistdemo.util.categories
import com.nassef.shoppinglistdemo.util.getItemCategoryByName
import com.nassef.shoppinglistdemo.util.gradientColors

//@Preview
@Composable
fun MainScreen(modifier: Modifier = Modifier, viewModel: ShoppingListViewModel = hiltViewModel()) {

    val uiState = viewModel.uiState.collectAsState()
    val selectedCategoryName = rememberSaveable {
        mutableStateOf(categories[0].name)
    }
    val selectedCategory = getItemCategoryByName(selectedCategoryName.value)
    var newItemName by rememberSaveable {
        mutableStateOf("")
    }
    val showAddItemView = rememberSaveable {
        mutableStateOf(false)
    }
    val showItemDetailsView = rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(uiState.value.isSuccess) {
        if (uiState.value.isSuccess) {
            newItemName = ""
            selectedCategoryName.value = categories[0].name
            viewModel.resetSuccess()
        }
    }

    ScreenWithLoadingDialog(uiState.value.isLoading) {
        Column(modifier = Modifier.fillMaxSize()) {
            GroceryListHeader()
            AddingItemContainer(
                selectedCategory = selectedCategory!!,
                newItemName = newItemName,
                isVisible = showAddItemView.value,
                onSaveItem = {
                    viewModel.addShoppingItem(newItemName, selectedCategory, false)
                },
                showHideAddItem = {
                    showAddItemView.value = showAddItemView.value.not()
                    if (showAddItemView.value)
                        showItemDetailsView.value = false
                },
                onValueChanged = { name ->
                    newItemName = name
                }) { category ->
                selectedCategoryName.value = category.name
            }
            if (uiState.value.shoppingList.isNotEmpty())
                ItemsList(
                    items = uiState.value.shoppingList,
                    onDeleteItem = { viewModel.deleteItem(it) },
                    onEditItem = { viewModel.upsertShoppingItem(it) },
                    onFilterByCategory = { viewModel.filterShoppingList(it) },
                    onResetFilter = { viewModel.resetFiltering() }
                ) {
                    showAddItemView.value = false
                }
            else
                EmptyShoppingList()

        }
    }

}

@Preview
@Composable
fun EmptyShoppingList() {
    Column(
        modifier = Modifier
            .background(TransParentGray)
            .fillMaxWidth()
            .padding(top = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(R.drawable.shopping_cart),
            contentDescription = "cart",
            modifier = Modifier.size(40.dp),
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(R.string.your_grocery_list_is_empty),
            style = TextStyle.Default.copy(
                color = Color.Gray,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.add_items_above_to_get_started),
            style = TextStyle.Default.copy(
                color = Color.LightGray,
                fontWeight = FontWeight.ExtraBold
            )
        )

    }
}

@Preview
@Composable
fun GroceryListHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = gradientColors
                    ), shape = CircleShape
                )
                .size(50.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.shopping_cart),
                contentDescription = "",
                modifier = Modifier.size(20.dp),
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(R.string.grocery_list),
            style = TextStyle.Default.copy(fontWeight = FontWeight.W900, fontSize = 20.sp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(R.string.add_items_to_your_shopping_list),
            style = TextStyle.Default.copy(color = Color.LightGray)
        )
        Spacer(modifier = Modifier.height(20.dp))
    }

}

@Preview
@Composable
fun AddingItemContainer(
    modifier: Modifier = Modifier,
    selectedCategory: Category = categories[0],
    newItemName: String = "",
    isVisible: Boolean = true,
    onSaveItem: () -> Unit = {},
    showHideAddItem: () -> Unit = {},
    onValueChanged: (String) -> Unit = {},
    onCategorySelected: (Category) -> Unit = {}
) {

    Box(modifier = modifier.fillMaxWidth()) {
        if (isVisible) {
            ItemDetailsCard(
                modifier = Modifier,
                newItemName,
                onValueChanged,
                selectedCategory,
                onCategorySelected,
            ) {
                onSaveItem()
            }
        }

        Box(
            modifier = Modifier.background(
                brush = Brush.horizontalGradient(
                    colors = gradientColors
                ), shape = RoundedCornerShape(10.dp)
            )
        ) {
            Button(
                onClick = { showHideAddItem() },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors().copy(containerColor = Color.Transparent),
            ) {
                Text(
                    stringResource(R.string.add_new_item),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(1f)
                )
                if (isVisible)
                    Icon(
                        painterResource(R.drawable.arrow_up),
                        contentDescription = "fold",
                        modifier = Modifier.size(15.dp)
                    )
                else
                    Icon(
                        painterResource(R.drawable.arrow_down),
                        contentDescription = "expand",
                        modifier = Modifier.size(15.dp)
                    )
            }
        }

    }
}

@Composable
private fun ItemDetailsCard(
    modifier: Modifier = Modifier,
    newItemName: String,
    onValueChanged: (String) -> Unit,
    selectedCategory: Category,
    onCategorySelected: (Category) -> Unit,
    isEditItem: Boolean = false,
    onPurchasedClicked: (Boolean) -> Unit = {},
    isItemPurchased: Boolean = false,
    onSaveItem: () -> Unit,

    ) {
    Card(
        modifier = modifier
//            .padding()
            .fillMaxWidth()
            .padding(horizontal = 2.dp)
            .padding(top = 35.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(15.dp)) {
            Text(
                text = stringResource(R.string.item_name),
                modifier = Modifier.padding(top = 20.dp),
                style = TextStyle.Default.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = newItemName,
                onValueChange = { onValueChanged(it) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                label = {
                    Text(text = stringResource(R.string.enter_grocery_item))
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = TransParentGray,
                    unfocusedContainerColor = TransParentGray
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            if (isEditItem)
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(checked = isItemPurchased, onCheckedChange = {
                        onPurchasedClicked(it)
                    })

                    Text(
                        text = stringResource(R.string.purchased),
                        style = TextStyle.Default.copy(
                            fontWeight = FontWeight.ExtraBold
                        )
                    )
                }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.category),
                style = TextStyle.Default.copy(fontWeight = FontWeight.ExtraBold)
            )
            CategoriesList(selectedCategory, onCategorySelected)
            Button(
                onClick = { onSaveItem() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                var icon = painterResource(R.drawable.plus)
                var buttonLabel = stringResource(R.string.add_item)
                if (isEditItem) {
                    icon = painterResource(R.drawable.save)
                    buttonLabel = stringResource(R.string.save_changes)
                }
                Icon(
                    painter = icon,
                    "",
                    modifier = Modifier
                        .padding(5.dp)
                        .size(15.dp)
                )
                Text(text = buttonLabel)
            }
        }
    }
}

@Composable
private fun CategoriesList(
    selectedCategory: Category?,
    onCategorySelected: (Category) -> Unit
) {
    LazyRow(modifier = Modifier.fillMaxWidth(), state = rememberLazyListState()) {
        items(categories) {
            ItemCategory(
                category = it,
                isSelected = selectedCategory == it
            ) {
                onCategorySelected(it)
            }
        }
    }
}

@Composable
fun ItemCategory(
    modifier: Modifier = Modifier,
    category: Category,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bkColor = if (isSelected) LightBlue else Color(category.bkColor)

    Column(
        modifier = modifier
            .padding(15.dp)
            .background(color = bkColor, shape = RoundedCornerShape(15.dp))
            .padding(10.dp)
            .clickable(onClick = onClick),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource(category.icon),
            contentDescription = "category icon",
            modifier = Modifier
                .padding(5.dp)
                .size(25.dp))
        Text(text = category.name, modifier = Modifier.padding(5.dp))
    }
}

val dummyItemList = listOf(
    Item(0, "ss", categories[1], false),
    Item(1, "ss", categories[1], false),
    Item(2, "ss", categories[1], false),
    Item(3, "ss", categories[1], false)
)

@Preview(name = "list of items")
@Composable
fun ItemsList(
    modifier: Modifier = Modifier,
    items: List<Item> = dummyItemList,
    onDeleteItem: (Item) -> Unit = {},
    onEditItem: (Item) -> Unit = {},
    onFilterByCategory: (Category) -> Unit = {},
    onResetFilter: () -> Unit = {},
    hideAddItemView: () -> Unit = {}
) {
    val showFiltration = rememberSaveable {
        mutableStateOf(false)
    }
    val selectedCategoryName = rememberSaveable {
        mutableStateOf<String>("")
    }
    val selectedCategory = getItemCategoryByName(selectedCategoryName.value)
    Column() {
        Row(
            modifier = Modifier
                .align(
                    Alignment.End
                )
        ) {
            IconButton(
                modifier = Modifier, onClick = {
                    showFiltration.value = showFiltration.value.not()
                }) {
                Icon(
                    painter = painterResource(R.drawable.funnel),
                    contentDescription = "filter",
                    modifier = Modifier
                        .padding(10.dp)
                        .size(15.dp)
                )
            }
            IconButton(
                modifier = Modifier, onClick = {
                    selectedCategoryName.value = ""
                    onResetFilter()
                }) {
                Icon(
                    painter = painterResource(R.drawable.eraser),
                    contentDescription = "delete filter",
                    modifier = Modifier
                        .padding(10.dp)
                        .size(15.dp)
                )
            }
        }

        if (showFiltration.value) {
            CategoriesList(selectedCategory) {
                selectedCategoryName.value = it.name
                onFilterByCategory(it)
            }
        }

        LazyColumn(modifier = modifier, state = rememberLazyListState()) {
            items(items, key = { it.id }) {
                SwipeItemView(
                    item = it,
                    onToggleDone = {},
                    onRemove = { onDeleteItem(it) },
                    onEditItem = { updatedItem -> onEditItem(updatedItem) }
                ) {
                    hideAddItemView()
                }
            }
        }
    }
}

@Composable
fun SwipeItemView(
    item: Item,
    onToggleDone: (Item) -> Unit,
    onRemove: (Item) -> Unit,
    onEditItem: (Item) -> Unit,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    /*val swipeToDismissBoxState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.StartToEnd) onToggleDone(item)
            else if (it == SwipeToDismissBoxValue.EndToStart) onRemove(item)
            // Reset item when toggling done status
            it != SwipeToDismissBoxValue.StartToEnd
        }
    )*/
    val swipeToDismissBoxState = rememberSwipeToDismissBoxState()

    SwipeToDismissBox(
        state = swipeToDismissBoxState,
        modifier = modifier.fillMaxSize(),
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            when (swipeToDismissBoxState.dismissDirection) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    /*Icon(
                        if (item.completionStatus) painterResource(R.drawable.shopping_cart) else painterResource(
                            R.drawable.shopping_cart
                        ),
                        contentDescription = if (item.completionStatus) "Done" else "Not done",
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Blue)
                            .wrapContentSize(Alignment.CenterStart)
                            .padding(12.dp),
                        tint = Color.White
                    )*/
                }

                SwipeToDismissBoxValue.EndToStart -> {
                    Icon(
                        painterResource(R.drawable.trash_icon),
                        contentDescription = "Remove item",
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Red)
                            .wrapContentSize(Alignment.CenterEnd)
                            .padding(12.dp),
                        tint = Color.White
                    )
                }

                SwipeToDismissBoxValue.Settled -> {}
            }
        },
        onDismiss = {
            if (it == SwipeToDismissBoxValue.StartToEnd) onToggleDone(item)
            else if (it == SwipeToDismissBoxValue.EndToStart) onRemove(item)
            // Reset item when toggling done status
            it != SwipeToDismissBoxValue.StartToEnd
        }
    ) {
        ItemView(item = item, onEditItem = onEditItem) {
            onClick()
        }
    }
}


@Preview
@Composable
fun ItemView(
    modifier: Modifier = Modifier,
    item: Item = Item(0, "test", categories[0], false),
    onEditItem: (Item) -> Unit = {},
    onClick: () -> Unit = {}
) {

    val showDetails = rememberSaveable {
        mutableStateOf(false)
    }
    val itemName = rememberSaveable {
        mutableStateOf(item.name)
    }
    val itemCategoryName = rememberSaveable {
        mutableStateOf(item.category.name)
    }
    val isItemPurchased = rememberSaveable {
        mutableStateOf(item.completionStatus)
    }
    val category = getItemCategoryByName(itemCategoryName.value)
    Box(modifier = Modifier.background(TransParentGray)) {
        if (showDetails.value)
            ItemDetailsCard(
                modifier = Modifier.padding(top = 20.dp),
                newItemName = itemName.value,
                onValueChanged = {
                    itemName.value = it
                },
                selectedCategory = category!!,
                onCategorySelected = {
                    itemCategoryName.value = it.name
                },
                isEditItem = true,
                onPurchasedClicked = {
                    isItemPurchased.value = it
                },
                isItemPurchased = isItemPurchased.value
            ) {
                onEditItem(
                    item.copy(
                        name = itemName.value,
                        category = category,
                        completionStatus = isItemPurchased.value
                    )
                )
            }

        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clickable(onClick = {
                    onClick()
                    showDetails.value = showDetails.value.not()
                }),
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
            /*colors = CardDefaults.cardColors(containerColor = Color.Blue)*/
        ) {
            Row(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                ItemIdentifier(
                    modifier = Modifier.weight(1f),
                    name = item.name,
                    label = stringResource(R.string.item_name)
                )
                ItemIdentifier(
                    name = item.category.name,
                    label = stringResource(R.string.category),
                    modifier = Modifier
                        .background(
                            Color(item.category.bkColor),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(8.dp)
                )
            }
        }

    }


}

@Composable
fun ItemIdentifier(modifier: Modifier = Modifier, name: String, label: String) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = label,
            style = TextStyle.Default.copy(
                color = Color.Gray,
                fontWeight = FontWeight.ExtraBold
            )
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = name,
            style = TextStyle.Default.copy(fontSize = 15.sp, fontWeight = FontWeight.Medium)
        )
    }
}
