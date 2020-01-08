package com.example.fithub

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.CompactCalendarView.CompactCalendarViewListener
import com.github.sundeepk.compactcalendarview.EventsContainer
import com.github.sundeepk.compactcalendarview.domain.Event
import kotlinx.android.synthetic.main.fragment_home.*
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment:Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

    return inflater.inflate(R.layout.fragment_home, container, false)}

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val compactCalendar: CompactCalendarView = view?.findViewById(R.id.calendarViewCalendar)!!
        val dateFormatMonth = SimpleDateFormat("MM yyyy", Locale.getDefault())

        compactCalendar.setUseThreeLetterAbbreviation(true)

        val eventColor = arrayOf(Color.BLUE, Color.RED, Color.CYAN, Color.GREEN, Color.TRANSPARENT)
        val eventData = arrayOf("Aerobic Exercise done", "Core Exercise done", "Arm Exercise done", "Leg Exercise done", "No record...")

        //set date into 12mn in term milliseconds
        val c = GregorianCalendar()
        c.set(Calendar.HOUR_OF_DAY, 0) //anything 0 - 23
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)
        val currentTimeInLong = c.time.time

        textViewMonth.text = dateFormatMonth.format(c.time)

        //testing purpose
        //val event1 = Event(Color.BLUE, 1578537000000L, "test")
        //compactCalendar.addEvent(event1)
        //set current event //milliseconds in one week-604800000L
        //val event2 = Event(Color.RED, currentTimeInLong - 604800000L, "test2")
        //compactCalendar.addEvent(event2)

        //set event
        var events = ArrayList<Event>()
        var event:Event
        val i = 0
        //if(i == 0 || i == 1 || i == 2 || i == 3){
            event = Event(eventColor[i], currentTimeInLong - 86400000L, eventData[i])
            events.add(event)
            event = Event(eventColor[1], currentTimeInLong - 86400000L, eventData[1])
            events.add(event)
            event = Event(eventColor[2], currentTimeInLong - 86400000L, eventData[2])
            events.add(event)
            compactCalendar.addEvents(events)
        //}
        //else{
        //    event = Event(eventColor[4], currentTimeInLong, eventData[4])
        //}
        //compactCalendar.addEvents(events)
        //compactCalendar.addEvent(event)


        compactCalendar.setListener(object : CompactCalendarViewListener{
            override fun onDayClick(dateClicked: Date?) {
                if(dateClicked?.time == event.timeInMillis){
                    var eventString = ""
                    for(event in compactCalendar.getEvents(dateClicked.time)){
                        eventString += event.data.toString() + "\n"
                    }
                    textViewEvent.text = eventString
                }else{
                    textViewEvent.text = eventData[4]
                }
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                textViewMonth.text = dateFormatMonth.format(firstDayOfNewMonth)
            }
        })
    }
}
