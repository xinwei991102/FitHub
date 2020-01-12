package com.example.fithub

import android.annotation.TargetApi
import android.content.SharedPreferences
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.CompactCalendarView.CompactCalendarViewListener
import com.github.sundeepk.compactcalendarview.domain.Event
import com.google.firebase.auth.FirebaseAuth
import java.util.*
import kotlin.collections.ArrayList
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BulletSpan
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.*
import java.nio.file.Files.exists

class HomeFragment:Fragment() {
    private lateinit var pref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var mSSBuilder:SpannableStringBuilder
    private lateinit var textViewEvent:TextView
    private lateinit var textViewMonth:TextView
    private lateinit var calendarView: CompactCalendarView
    private lateinit var exerciseRecord:ExerciseRecord
    // Write a message to the database
    private val recordDB = FirebaseDatabase.getInstance()
    private val recordRef = recordDB.getReference("ExerciseRecord")
    //array
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

    lateinit var eventString:String
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(Build.VERSION_CODES.N)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        eventString = getString(R.string.no_date)
        calendarView = view?.findViewById(R.id.calendarView)!!
        calendarView.setUseThreeLetterAbbreviation(true)
        val dateFormatMonth = SimpleDateFormat("MM yyyy", Locale.getDefault())
        val currentUId = FirebaseAuth.getInstance().currentUser?.uid

        //set date into 12mn in term milliseconds
        val c = GregorianCalendar()
        c.set(Calendar.HOUR_OF_DAY, 0) //anything 0 - 23
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)
        val currentTimeInLong = c.time.time

        textViewMonth.text = dateFormatMonth.format(c.time)
        pref = requireContext().getSharedPreferences(currentUId, 0) // 0 - for private mode
        editor = pref.edit()

        //set event
        var events = ArrayList<Event>()
        var event:Event
        var evColor = 0
        var eventData = ""

        //TODO get data from database and add into event
        //1. get data
        //2. write into event and events
        //3. write into calendar
        val eventRecordQuery = recordRef.orderByChild("uid").equalTo(currentUId)
        eventRecordQuery.addChildEventListener(object: ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context,p0.message,Toast.LENGTH_LONG)
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                if(!dataSnapshot.exists()){
                    calendarView.addEvents(events)
                    Toast.makeText(context, "calendar", Toast.LENGTH_SHORT).show()
                }
                Toast.makeText(context, "onChildAdded", Toast.LENGTH_SHORT).show()
                evColor = dataSnapshot.getValue(ExerciseRecord::class.java)?.evColor!!
                val time = dataSnapshot.getValue(ExerciseRecord::class.java)?.millisecond!!
                eventData = dataSnapshot.getValue(ExerciseRecord::class.java)?.data!!
                event = Event(eventColor[evColor], time, eventData)
                events.add(event)
            }
        })

        if(pref.getString("completed_exercise_name", "").isNotEmpty()){
            when (pref.getString("completed_exercise_name", "")) {
                getString(R.string.aerobic_exercise) -> {
                    evColor = 0
                    eventData = getString(R.string.aerobic_exercise)
                }
                getString(R.string.core_exercise) -> {
                    evColor = 1
                    eventData = getString(R.string.core_exercise)
                }
                getString(R.string.arm_exercise) -> {
                    evColor = 2
                    eventData = getString(R.string.arm_exercise)
                }
                getString(R.string.leg_exercise) -> {
                    evColor = 3
                    eventData = getString(R.string.leg_exercise)
                }
                getString(R.string.testing_exercise) -> {
                    eventData = getString(R.string.testing_exercise)
                }
            }
            event = Event(eventColor[evColor], currentTimeInLong, eventData)
            events.add(event)
            //log record into database
            if (currentUId != null) {
                logRecord(currentUId, evColor, currentTimeInLong, eventData)

                calendarView.addEvents(events)
                Toast.makeText(context, "log record - add calendar", Toast.LENGTH_SHORT).show()
            }

            editor.remove("completed_exercise_name")
            editor.apply()
        }


        //assign text into mSSBuilder
        mSSBuilder = SpannableStringBuilder(eventString)
        // Display the spannable text to TextView
        textViewEvent.text = mSSBuilder // text view display no date selected

        calendarView.setListener(object : CompactCalendarViewListener{
            override fun onDayClick(dateClicked: Date?) {
                eventString = getString(R.string.no_record) //no record...
                if (calendarView.getEvents(dateClicked?.time!!).isNotEmpty()) {
                    eventString = getString(R.string.congrate)
                    for(event in calendarView.getEvents(dateClicked.time)){
                        eventString += "\n" + event.data.toString()
                    }
                }else if(calendarView.getEvents(dateClicked.time).isEmpty() && dateClicked.time == currentTimeInLong){
                    eventString += "\n" + getString(R.string.start_activity)
                }
                mSSBuilder = SpannableStringBuilder(eventString)
                for(event in calendarView.getEvents(dateClicked.time)){
                    when (event.data.toString()) {
                        "Aerobic Exercise" -> {
                            showBullet("Aerobic Exercise")  // Generate bulleted list
                        }
                        "Core Exercise" -> {
                            showBullet("Core Exercise")
                        }
                        "Arm Exercise" -> {
                            showBullet("Arm Exercise")
                        }
                        "Leg Exercise" -> {
                            showBullet("Leg Exercise")
                        }
                        "testing exercise" -> {
                            showBullet("testing exercise")
                        }
                    }
                }
                textViewEvent.text = mSSBuilder //eventString
            }
            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                textViewMonth.text = dateFormatMonth.format(firstDayOfNewMonth)
                textViewEvent.text = null
            }
        })
    }

    // Custom method to generate a bulleted list
    private fun showBullet(textToBullet:String ){
        // Initialize a new BulletSpan
        var bulletSpan = BulletSpan(0,Color.TRANSPARENT)
        when (textToBullet) {
            "Aerobic Exercise" -> {
                bulletSpan = BulletSpan(100, eventColor[0])
            }
            "Core Exercise" -> {
                bulletSpan = BulletSpan(100, eventColor[1])
            }
            "Arm Exercise" -> {
                bulletSpan = BulletSpan(100, eventColor[2])
            }
            "Leg Exercise" -> {
                bulletSpan = BulletSpan(100, eventColor[3])
            }
            "testing exercise" -> {
                bulletSpan = BulletSpan(100, eventColor[0])
            }
        }

        // Apply the bullet to the span
        mSSBuilder.setSpan(
            bulletSpan, // Span to add
            eventString.indexOf(textToBullet), // Start of the span (inclusive)
            eventString.indexOf(textToBullet) + 1,  // End of the span (exclusive)
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE // Do not extend the span when text add later
        )
    }

    private fun logRecord(uid:String, evColor:Int, time:Long, data:String){
        exerciseRecord = ExerciseRecord(uid, evColor, time, data)
        recordRef.push().setValue(exerciseRecord)
    }

}
