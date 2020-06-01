package com.example.githubrepo.util

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.githubrepo.R
import com.example.githubrepo.data.remote.model.Permissions
import kotlinx.android.synthetic.main.repo_item_view.view.*
import java.lang.StringBuilder

@BindingAdapter("permissionText")
fun permissionText(view: TextView, permissions: Permissions?) {
    var text = StringBuilder(view.context.getString(R.string.permission))
    permissions?.let {
        if (it.admin)
            text.append(view.context.getString(R.string.permission_admin))
        if (it.push)
            text.append(view.context.getString(R.string.permission_push))
        if (it.pull)
            text.append(view.context.getString(R.string.permission_pull))
        view.text = text
    } ?: view.gone()
}