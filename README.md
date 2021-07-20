Android Feeds Application

Functionality:
The app contains two layouts 
*Staggered Grid View-> All functionality has been attached to this recycle view including:
1. Add image with camera menu button.
2. Search button on change listener.
3. Share button that includes implicit intent.
4. On touch grid item listener which pops a bottom sheet for delete confirmation which sends those items to Trash tab.
5. Rearrangement of items through all directions.
6. Trash tab can be reached by menu bar and directs the user to a list view with time of deletion.
7. Trashed items can be deleted permanently by swiping to the right.
8. Counter that deletes feeds when it exceeds 10 feeds limit.
9. Empty screen says "NO FEEDS AVAILABLE" when list array length has 0 size.
*Fixed Grid View.

Techniques:
*Adapters.
*Caching images with picasso (green arrows means caching via cache and blue means storage cache).
*Firebase real time database.
*Firebase storage.



