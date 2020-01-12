package com.example.fithub

import android.annotation.TargetApi
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BulletSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.CompactCalendarView.CompactCalendarViewListener
import com.github.sundeepk.compactcalendarview.domain.Event
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {
    private lateinit var mSSBuilder: SpannableStringBuilder
    private lateinit var textViewEvent: TextView
    private lateinit var textViewMonth: TextView
    private lateinit var calendarView: CompactCalendarView
    // Write a message to the database
    private val recordDB = FirebaseDatabase.getInstance()
    private val recordRef = recordDB.getReference("ExerciseRecord")
    //event colours
    private val eventColor = arrayOf(Color.BLUE, Color.RED, Color.CYAN, Color.GREEN)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        textViewMonth = view.findViewById(R.id.textViewMonth)
        textViewEvent = view.findViewById(R.id.textViewEvent)
        return view
    }

    lateinit var eventString: String
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(Build.VERSION_CODES.N)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        eventString = getString(R.string.no_date)
        calendarView = view?.findViewById(R.id.calendarView)!!
        calendarView.setUseThreeLetterAbbreviation(true)
        val dateFormatMonth = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        val currentUId = FirebaseAuth.getInstance().currentUser?.uid

        //set date into 12am midnight in term milliseconds
        val c = GregorianCalendar()
        c.set(Calendar.HOUR_OF_DAY, 0) //anything 0 - 23
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)
        val currentTimeInLong = c.time.time
        textViewMonth.text = dateFormatMonth.format(c.time)

        //set event
        val events = ArrayList<Event>()
        var event: Event
        var evColor: Int
        var eventData: String

        //TODO get data from database and add into event
        //1. get data
        //2. write into event and events
        //3. write into calendar
        val eventRecordQuery = recordRef.orderByChild("uid").equalTo(currentUId)
        eventRecordQuery.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, p0.message, Toast.LENGTH_LONG)
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                //TODO("not implemented")
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                //TODO("not implemented")
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                //TODO("not implemented")
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                if (context != null) {
                    if (!dataSnapshot.exists()) {
                        Toast.makeText(context, "Data snapshot does not exist", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        evColor = dataSnapshot.getValue(ExerciseRecord::class.java)?.evColor!!
                        val time = dataSnapshot.getValue(ExerciseRecord::class.java)?.millisecond!!
                        eventData = dataSnapshot.getValue(ExerciseRecord::class.java)?.data!!
                        event = Event(eventColor[evColor], time, eventData)
                        events.add(event)
                        calendarView.removeAllEvents()
                        calendarView.addEvents(events)
                    }
                }
            }
        })

        //assign text into mSSBuilder
        mSSBuilder = SpannableStringBuilder(eventString)
        // Display the spannable text to TextView
        textViewEvent.text = mSSBuilder // text view display no date selected

        calendarView.setListener(object : CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date?) {
                eventString = getString(R.string.no_record) //no record...
                if (calendarView.getEvents(dateClicked?.time!!).isNotEmpty()) {
                    eventString = getString(R.string.congrate)
                    for (event in calendarView.getEvents(dateClicked.time)) {
                        val eventDataString = event.data.toString().replace("\n", " ")
                        //if same exercise have not added before
                        if (eventString.indexOf(eventDataString) == -1) {
                            eventString += " \n" + event.data.toString().replace("\n", " ")
                        }
                    }
                } else if (calendarView.getEvents(dateClicked.time).isEmpty() && dateClicked.time == currentTimeInLong) {
                    eventString += "\n" + getString(R.string.start_activity)
                }
                mSSBuilder = SpannableStringBuilder(eventString)
                for (event in calendarView.getEvents(dateClicked.time)) {
                    val eventDataString = event.data.toString().replace("\n", " ")
                    showBullet(eventDataString, event.color)
                }
                textViewEvent.text = mSSBuilder //eventString
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                textViewMonth.text = dateFormatMonth.format(firstDayOfNewMonth)
                textViewEvent.text = null
            }
        })
    }

    // Custom method to generate a bullet point list
    private fun showBullet(textToBullet: String, evColor: Int) {
        // Initialize a new BulletSpan
        val bulletSpan = BulletSpan(20, evColor)
        // Apply the bullet to the span
        val startIndex = eventString.indexOf(textToBullet)  // Start of the span (inclusive)
        val endIndex = startIndex + 1  // End of the span (exclusive)
        if (startIndex != -1) {
            mSSBuilder.setSpan(
                bulletSpan, // Span to add
                startIndex, // Start of the span (inclusive)
                endIndex,  // End of the span (exclusive)
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE // Do not extend the span when text add later
            )
        }

    }

}
