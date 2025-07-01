"use client"

import { useState } from "react"
import { Card, CardContent } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Checkbox } from "@/components/ui/checkbox"
import { Home, FileText, CheckSquare, Plus, User, UserPlus } from "lucide-react"

export default function NotionHomepage() {
    const [activeSection, setActiveSection] = useState("home")
    const [newNote, setNewNote] = useState("")
    const [newTask, setNewTask] = useState("")
    const [notes, setNotes] = useState(["Welcome to your workspace", "Start writing your ideas here"])
    const [tasks, setTasks] = useState([
        { text: "Learn HTML & CSS", done: true },
        { text: "Build a Notion Clone", done: false },
        { text: "Explore JavaScript", done: false },
    ])

    const addNote = () => {
        if (newNote.trim()) {
            setNotes([...notes, newNote])
            setNewNote("")
        }
    }

    const addTask = () => {
        if (newTask.trim()) {
            setTasks([...tasks, { text: newTask, done: false }])
            setNewTask("")
        }
    }

    const toggleTask = (index: number) => {
        const updatedTasks = [...tasks]
        updatedTasks[index].done = !updatedTasks[index].done
        setTasks(updatedTasks)
    }

    return (
        <div className="min-h-screen bg-white">
            {/* Header */}
            <header className="border-b border-gray-200 px-6 py-4">
                <div className="flex items-center justify-between max-w-6xl mx-auto">
                    <div className="flex items-center gap-2">
                        <div className="w-8 h-8 bg-black rounded flex items-center justify-center">
                            <span className="text-white font-bold text-sm">N</span>
                        </div>
                        <span className="text-xl font-semibold">My Notion</span>
                    </div>
                    <div className="flex items-center gap-3">
                        <Button variant="ghost" size="sm">
                            <User className="w-4 h-4 mr-2" />
                            로그인
                        </Button>
                        <Button size="sm" className="bg-black hover:bg-gray-800">
                            <UserPlus className="w-4 h-4 mr-2" />
                            회원가입
                        </Button>
                    </div>
                </div>
            </header>

            <div className="flex max-w-6xl mx-auto">
                {/* Sidebar */}
                <aside className="w-64 p-6 border-r border-gray-200 min-h-screen">
                    <nav className="space-y-1">
                        <button
                            onClick={() => setActiveSection("home")}
                            className={`w-full flex items-center gap-3 px-3 py-2 rounded-md text-left transition-colors ${
                                activeSection === "home" ? "bg-gray-100 text-black" : "text-gray-600 hover:bg-gray-50"
                            }`}
                        >
                            <Home className="w-4 h-4" />
                            Home
                        </button>
                        <button
                            onClick={() => setActiveSection("notes")}
                            className={`w-full flex items-center gap-3 px-3 py-2 rounded-md text-left transition-colors ${
                                activeSection === "notes" ? "bg-gray-100 text-black" : "text-gray-600 hover:bg-gray-50"
                            }`}
                        >
                            <FileText className="w-4 h-4" />
                            Notes
                        </button>
                        <button
                            onClick={() => setActiveSection("tasks")}
                            className={`w-full flex items-center gap-3 px-3 py-2 rounded-md text-left transition-colors ${
                                activeSection === "tasks" ? "bg-gray-100 text-black" : "text-gray-600 hover:bg-gray-50"
                            }`}
                        >
                            <CheckSquare className="w-4 h-4" />
                            Tasks
                        </button>
                    </nav>
                </aside>

                {/* Main Content */}
                <main className="flex-1 p-8">
                    {activeSection === "home" && (
                        <div className="space-y-8">
                            <div>
                                <h1 className="text-3xl font-bold mb-2">Welcome to Notion Clone</h1>
                                <p className="text-gray-600">Organize your thoughts and tasks in one place.</p>
                            </div>

                            <div className="grid md:grid-cols-2 gap-6">
                                {/* Notes Preview */}
                                <Card className="border border-gray-200">
                                    <CardContent className="p-6">
                                        <div className="flex items-center gap-2 mb-4">
                                            <FileText className="w-5 h-5" />
                                            <h2 className="text-lg font-semibold">📝 Notes</h2>
                                        </div>
                                        <div className="space-y-2">
                                            {notes.slice(0, 3).map((note, index) => (
                                                <p key={index} className="text-gray-700 text-sm">
                                                    • {note}
                                                </p>
                                            ))}
                                        </div>
                                        <Button
                                            variant="ghost"
                                            size="sm"
                                            className="mt-4 text-blue-600 hover:text-blue-700"
                                            onClick={() => setActiveSection("notes")}
                                        >
                                            View all notes →
                                        </Button>
                                    </CardContent>
                                </Card>

                                {/* Tasks Preview */}
                                <Card className="border border-gray-200">
                                    <CardContent className="p-6">
                                        <div className="flex items-center gap-2 mb-4">
                                            <CheckSquare className="w-5 h-5" />
                                            <h2 className="text-lg font-semibold">📌 To-Do List</h2>
                                        </div>
                                        <div className="space-y-3">
                                            {tasks.slice(0, 3).map((task, index) => (
                                                <div key={index} className="flex items-center gap-2">
                                                    <Checkbox checked={task.done} className="w-4 h-4" />
                                                    <span className={`text-sm ${task.done ? "line-through text-gray-500" : "text-gray-700"}`}>
                            {task.text}
                          </span>
                                                </div>
                                            ))}
                                        </div>
                                        <Button
                                            variant="ghost"
                                            size="sm"
                                            className="mt-4 text-blue-600 hover:text-blue-700"
                                            onClick={() => setActiveSection("tasks")}
                                        >
                                            View all tasks →
                                        </Button>
                                    </CardContent>
                                </Card>
                            </div>
                        </div>
                    )}

                    {activeSection === "notes" && (
                        <div className="space-y-6">
                            <div>
                                <h1 className="text-2xl font-bold mb-4">📝 Notes</h1>
                                <div className="flex gap-2">
                                    <Input
                                        placeholder="Write a new note..."
                                        value={newNote}
                                        onChange={(e) => setNewNote(e.target.value)}
                                        onKeyPress={(e) => e.key === "Enter" && addNote()}
                                        className="flex-1"
                                    />
                                    <Button onClick={addNote}>
                                        <Plus className="w-4 h-4 mr-2" />
                                        Add
                                    </Button>
                                </div>
                            </div>

                            <div className="space-y-3">
                                {notes.map((note, index) => (
                                    <Card key={index} className="border border-gray-200">
                                        <CardContent className="p-4">
                                            <p className="text-gray-700">{note}</p>
                                        </CardContent>
                                    </Card>
                                ))}
                            </div>
                        </div>
                    )}

                    {activeSection === "tasks" && (
                        <div className="space-y-6">
                            <div>
                                <h1 className="text-2xl font-bold mb-4">📌 Tasks</h1>
                                <div className="flex gap-2">
                                    <Input
                                        placeholder="Add a new task..."
                                        value={newTask}
                                        onChange={(e) => setNewTask(e.target.value)}
                                        onKeyPress={(e) => e.key === "Enter" && addTask()}
                                        className="flex-1"
                                    />
                                    <Button onClick={addTask}>
                                        <Plus className="w-4 h-4 mr-2" />
                                        Add
                                    </Button>
                                </div>
                            </div>

                            <div className="space-y-3">
                                {tasks.map((task, index) => (
                                    <Card key={index} className="border border-gray-200">
                                        <CardContent className="p-4">
                                            <div className="flex items-center gap-3">
                                                <Checkbox checked={task.done} onCheckedChange={() => toggleTask(index)} />
                                                <span className={`${task.done ? "line-through text-gray-500" : "text-gray-700"}`}>
                          {task.text}
                        </span>
                                            </div>
                                        </CardContent>
                                    </Card>
                                ))}
                            </div>
                        </div>
                    )}
                </main>
            </div>
        </div>
    )
}
