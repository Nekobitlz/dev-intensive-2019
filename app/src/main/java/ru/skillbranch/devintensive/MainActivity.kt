package ru.skillbranch.devintensive

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.models.Bender
import ru.skillbranch.devintensive.models.Bender.Question

class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var benderImage: ImageView
    lateinit var textTxt: TextView
    lateinit var messageEt: EditText
    lateinit var sendBtn: ImageView

    lateinit var benderObj: Bender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        benderImage = iv_bender
        textTxt = tv_text
        messageEt = et_message
        sendBtn = iv_send

        val status = savedInstanceState?.getString(STATUS_TAG) ?: Bender.Status.NORMAL.name
        val question = savedInstanceState?.getString(QUESTION_TAG) ?: Bender.Question.NAME.name
        benderObj = Bender(Bender.Status.valueOf(status), Question.valueOf(question))

        val(r, g, b) = benderObj.status.color
        benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)

        textTxt.text = benderObj.askQuestion()
        sendBtn.setOnClickListener(this)

        messageEt.setRawInputType(InputType.TYPE_CLASS_TEXT)
        messageEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE)
                sendBtn.performClick()

            false
        }
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.iv_send)
            if (isAnswerValid())
                sendSuccess()
            else
                sendError()
    }

    private fun sendError() {
        val errorMessage = benderObj.addValidationMessage(benderObj.question)

        textTxt.text = "$errorMessage\n${benderObj.question.question}"
        messageEt.setText("")
    }

    private fun isAnswerValid(): Boolean = benderObj.question.validate(messageEt.text.toString())

    private fun sendSuccess() {
        val (phase, color) = benderObj.listenAnswer(messageEt.text.toString().toLowerCase())
        val(r, g, b) = color

        messageEt.setText("")
        benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)
        textTxt.text = phase
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putString(STATUS_TAG, benderObj.status.name)
        outState?.putString(QUESTION_TAG, benderObj.question.name)
    }

    companion object {
        const val STATUS_TAG = "STATUS"
        const val QUESTION_TAG = "QUESTION"
    }
}
