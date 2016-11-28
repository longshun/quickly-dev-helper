
	xml中写法
	<com.hedgehog.ratingbar.RatingBar
    android:id="@+id/ratingbar"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_centerInParent="true"
    android:layout_gravity="center"
    android:layout_marginTop="50dp"
    hedgehog:clickable="true"
    hedgehog:halfstart="true"
    hedgehog:starCount="5"
    hedgehog:starEmpty="@mipmap/star_empty"
    hedgehog:starFill="@mipmap/star_full"
    hedgehog:starHalf="@mipmap/star_half"
    hedgehog:starImageHeight="90dp"
    hedgehog:starImageWidth="70dp"
    hedgehog:starImagePadding="20dp"/>
	
	代码中写法
	RatingBar mRatingBar = (RatingBar) findViewById(R.id.ratingbar);
    mRatingBar.setStarEmptyDrawable(getResources().getDrawable(R.mipmap.star_empty));
    mRatingBar.setStarHalfDrawable(getResources().getDrawable(R.mipmap.star_half));
    mRatingBar.setStarFillDrawable(getResources().getDrawable(R.mipmap.star_full));
    mRatingBar.setStarCount(5);
    mRatingBar.setStar(2.5f);
    mRatingBar.halfStar(true);
    mRatingBar.setmClickable(true);
    mRatingBar.setStarImageWidth(120f);
    mRatingBar.setStarImageHeight(60f);
    mRatingBar.setImagePadding(35);
    mRatingBar.setOnRatingChangeListener(
            new RatingBar.OnRatingChangeListener() {
                @Override
                public void onRatingChange(float RatingCount) {
                    Toast.makeText(MainActivity.this, "the fill star is" + RatingCount, Toast.LENGTH_SHORT).show();
                }
            }
    );

说明：
method	action
hedgehog:clickable="true"	Could you click
hedgehog:halfstart="true"	Whether to support half a star
hedgehog:starCount="N"	The total number of stars （int）
hedgehog:starEmpty="@mipmap/star_empty"	empty star
hedgehog:starFill="@mipmap/star_full"	full star
hedgehog:starHalf="@mipmap/star_half"	half star
hedgehog:starImageHeight="90dp"	the height of the stars
hedgehog:starImageWidth="70dp"	the width of the star
hedgehog:starImagePadding="20dp"	the padding of the star



method	action
mRatingBar.setStarCount(5);	The total number of stars
mRatingBar.setStar(2.5f);	Fill the total number of the stars
mRatingBar.halfStar(true);	Whether set up half a star
mRatingBar.setmClickable(true);	Whether the Settings can be click
mRatingBar.setStarImageWidth(120f);	the width of the stars
mRatingBar.setStarImageHeight(60f);	the height of the stars
mRatingBar.setImagePadding(35);	the padding of the stars