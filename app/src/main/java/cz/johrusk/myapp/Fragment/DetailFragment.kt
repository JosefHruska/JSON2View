package cz.johrusk.myapp.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cz.johrusk.myapp.R
import kotlinx.android.synthetic.main.detail_fragment.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * Fragment which shows title and body of item
 *
 * @author Josef Hruška (pepa.hruska@gmail.com)
 */
class DetailFragment : Fragment() {
    var body: String? = null
    var title: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getArguments().getString("title")
        body = getArguments().getString("body")

    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_detail_title.setText(Html.fromHtml(title))
        tv_detail_body.setText(Html.fromHtml(body))
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this); // Register EventBus instance to subscribe events
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this);
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.detail_fragment, container, false)
    }

    companion object {

        fun newInstance(): DetailFragment {

            val fragment = DetailFragment()

            return fragment
        }
    }

    // Gets body and title of selected items
    @Subscribe public fun onMessageEvent(event: MessageEvent) {
        if (event is MessageEvent) {
            tv_detail_title.setText(Html.fromHtml(event.title))
            tv_detail_body.setText(Html.fromHtml(event.body))
        }
    }
}