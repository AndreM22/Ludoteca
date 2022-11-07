package com.andremachicao.ludoteca

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

@BindingAdapter("srcUrl")
fun setImageSrcUrl(imageView: ImageView, url: String?) {
    if (url != null)
        Glide.with(imageView)
            .load(url)
            .into(imageView)

}
@BindingAdapter("srcUrl2")
fun setImageSrcUrl2(imageView: ImageView, url: String?) {
    if (url != null)
        Glide.with(imageView)
            .load(url)
            .transform(CenterCrop(), RoundedCorners(40))
            .into(imageView)
}
