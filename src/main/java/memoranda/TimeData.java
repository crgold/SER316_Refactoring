package main.java.memoranda;

import java.util.Vector;

import main.java.memoranda.date.CalendarDate;
import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Elements;

public class TimeData {
	
	public static TimeData timeData;
	
	public TimeData() { 
		timeData = new TimeData();
	}
	
	
	public static Day createDay(CalendarDate date, Element _root) {
		Year y = getYear(date.getYear(), _root);
		if (y == null)
			y = createYear(date.getYear(), _root);
		Month m = y.getMonth(date.getMonth());
		if (m == null)
			m = y.createMonth(date.getMonth());
		Day d = m.getDay(date.getDay());
		if (d == null)
			d = m.createDay(date.getDay());
		return d;
	}

	public static Year createYear(int y, Element _root) {
		Element el = new Element("year");
		el.addAttribute(new Attribute("year", new Integer(y).toString()));
		_root.appendChild(el);
		return new Year(el);
	}

	public static Year getYear(int y, Element _root) {
		Elements yrs = _root.getChildElements("year");
		String yy = new Integer(y).toString();
		for (int i = 0; i < yrs.size(); i++)
			if (yrs.get(i).getAttribute("year").getValue().equals(yy))
				return new Year(yrs.get(i));
		//return createYear(y);
		return null;
	}

	public static Day getDay(CalendarDate date, Element _root) {
		Year y = getYear(date.getYear(), _root);
		if (y == null)
			return null;
		Month m = y.getMonth(date.getMonth());
		if (m == null)
			return null;
		return m.getDay(date.getDay());
	}
	
	

static class Year {
	Element yearElement = null;

	public Year(Element el) {
		yearElement = el;
	}

	public int getValue() {
		return new Integer(yearElement.getAttribute("year").getValue())
			.intValue();
	}

	public Month getMonth(int m) {
		Elements ms = yearElement.getChildElements("month");
		String mm = new Integer(m).toString();
		for (int i = 0; i < ms.size(); i++)
			if (ms.get(i).getAttribute("month").getValue().equals(mm))
				return new Month(ms.get(i));
		//return createMonth(m);
		return null;
	}

	private Month createMonth(int m) {
		Element el = new Element("month");
		el.addAttribute(new Attribute("month", new Integer(m).toString()));
		yearElement.appendChild(el);
		return new Month(el);
	}

	public Vector getMonths() {
		Vector v = new Vector();
		Elements ms = yearElement.getChildElements("month");
		for (int i = 0; i < ms.size(); i++)
			v.add(new Month(ms.get(i)));
		return v;
	}

	public Element getElement() {
		return yearElement;
	}	
	}
static class Month {
	Element mElement = null;

	public Month(Element el) {
		mElement = el;
	}

	public int getValue() {
		return new Integer(mElement.getAttribute("month").getValue())
			.intValue();
	}

	public Day getDay(int d) {
		if (mElement == null)
			return null;
		Elements ds = mElement.getChildElements("day");
		String dd = new Integer(d).toString();
		for (int i = 0; i < ds.size(); i++)
			if (ds.get(i).getAttribute("day").getValue().equals(dd))
				return new Day(ds.get(i));
		//return createDay(d);
		return null;
	}

	private Day createDay(int d) {
		Element el = new Element("day");
		el.addAttribute(new Attribute("day", new Integer(d).toString()));
		el.addAttribute(
			new Attribute(
				"date",
				new CalendarDate(
					d,
					getValue(),
					new Integer(
						((Element) mElement.getParent())
							.getAttribute("year")
							.getValue())
						.intValue())
					.toString()));

		mElement.appendChild(el);
		return new Day(el);
	}

	public Vector getDays() {
		if (mElement == null)
			return null;
		Vector v = new Vector();
		Elements ds = mElement.getChildElements("day");
		for (int i = 0; i < ds.size(); i++)
			v.add(new Day(ds.get(i)));
		return v;
	}

	public Element getElement() {
		return mElement;
	}

}

static class Day {
	Element dEl = null;

	public Day(Element el) {
		dEl = el;
	}

	public int getValue() {
		return new Integer(dEl.getAttribute("day").getValue()).intValue();
	}

	/*
	 * public Note getNote() { return new NoteImpl(dEl);
	 */

	public Element getElement() {
		return dEl;
	}
}
}