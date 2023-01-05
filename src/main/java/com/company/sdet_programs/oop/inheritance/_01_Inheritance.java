package com.company.sdet_programs.oop.inheritance;

// In Java, an interface can inherit from multiple interfaces
interface  _01_Inheritance extends Car, Plane {}

interface Car {}

interface Plane {}





// In Java, a class can ONLY inherit from one other class using the extends keyword
// JavaScript has the same behaviour in relation to Interfaces and Classes
class _02_Inheritance extends House implements Mansion, SemiDetached {}

class House {}

class Flat {}

interface Mansion {}

interface SemiDetached {}