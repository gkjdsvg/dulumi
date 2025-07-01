"use client"

import { useState } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Badge } from "@/components/ui/badge"
import { Checkbox } from "@/components/ui/checkbox"
import { Home, FileText, CheckSquare, Plus, Edit3, Trash2, User, UserPlus, Search, Bell, Moon, Sun } from "lucide-react"

interface Note {
    id: string
    title: string
    content: string
    createdAt: Date
}

interface Task {
    id: string
    title: string
    completed: boolean
    createdAt: Date
}

export default function NotionClone() {
    const [activeTab, setActiveTab] = useState("home")
    const [notes, setNotes] = useState<Note[]>([
        {
            id: "1",
            title: "Welcome Note",
            content: "This is a beautiful Notion-like interface built with modern React!",
            createdAt: new Date(),
        },
    ])
    const [tasks, setTasks] = useState<Task[]>([
        { id: "1", title: "Learn HTML & CSS", completed: true, createdAt: new Date() },
        { id: "2", title: "Build a Notion Clone", completed: false, createdAt: new Date() },
        { id: "3", title: "Explore JavaScript", completed: false, createdAt: new Date() },
    ])
    const [newNoteTitle, setNewNoteTitle] = useState("")
    const [newTaskTitle, setNewTaskTitle] = useState("")
    const [editingNote, setEditingNote] = useState<string | null>(null)
    const [isDark, setIsDark] = useState(false)

    const addNote = () => {
        if (newNoteTitle.trim()) {
            const newNote: Note = {
                id: Date.now().toString(),
                title: newNoteTitle,
                content: "Start writing your thoughts here...",
                createdAt: new Date(),
            }
            setNotes([newNote, ...notes])
            setNewNoteTitle("")
        }
    }

    const addTask = () => {
        if (newTaskTitle.trim()) {
            const newTask: Task = {
                id: Date.now().toString(),
                title: newTaskTitle,
                completed: false,
                createdAt: new Date(),
            }
            setTasks([newTask, ...tasks])
            setNewTaskTitle("")
        }
    }

    const toggleTask = (id: string) => {
        setTasks(tasks.map((task) => (task.id === id ? { ...task, completed: !task.completed } : task)))
    }

    const deleteNote = (id: string) => {
        setNotes(notes.filter((note) => note.id !== id))
    }

    const deleteTask = (id: string) => {
        setTasks(tasks.filter((task) => task.id !== id))
    }

    const sidebarItems = [
        { id: "home", label: "Home", icon: Home },
        { id: "notes", label: "Notes", icon: FileText, count: notes.length },
        { id: "tasks", label: "Tasks", icon: CheckSquare, count: tasks.filter((t) => !t.completed).length },
    ]

    return (
        <div className={`min-h-screen ${isDark ? "dark bg-gray-900" : "bg-gray-50"} transition-colors duration-300`}>
            {/* Top Navigation */}
            <div className="border-b bg-white/80 backdrop-blur-sm sticky top-0 z-50 dark:bg-gray-800/80 dark:border-gray-700">
                <div className="flex items-center justify-between px-6 py-4">
                    <div className="flex items-center gap-3">
                        <div className="w-8 h-8 bg-gradient-to-br from-purple-500 to-blue-600 rounded-lg flex items-center justify-center">
                            <span className="text-white font-bold text-sm">N</span>
                        </div>
                        <h1 className="text-xl font-bold bg-gradient-to-r from-purple-600 to-blue-600 bg-clip-text text-transparent">
                            My Notion
                        </h1>
                    </div>

                    <div className="flex items-center gap-3">
                        <Button variant="ghost" size="sm" onClick={() => setIsDark(!isDark)}>
                            {isDark ? <Sun className="w-4 h-4" /> : <Moon className="w-4 h-4" />}
                        </Button>
                        <Button variant="ghost" size="sm">
                            <Search className="w-4 h-4" />
                        </Button>
                        <Button variant="ghost" size="sm">
                            <Bell className="w-4 h-4" />
                        </Button>
                        <Button variant="outline" size="sm">
                            <User className="w-4 h-4 mr-2" />
                            로그인
                        </Button>
                        <Button
                            size="sm"
                            className="bg-gradient-to-r from-purple-500 to-blue-600 hover:from-purple-600 hover:to-blue-700"
                        >
                            <UserPlus className="w-4 h-4 mr-2" />
                            회원가입
                        </Button>
                    </div>
                </div>
            </div>

            <div className="flex">
                {/* Sidebar */}
                <div className="w-64 bg-white border-r h-[calc(100vh-73px)] p-4 dark:bg-gray-800 dark:border-gray-700">
                    <nav className="space-y-2">
                        {sidebarItems.map((item) => {
                            const Icon = item.icon
                            return (
                                <button
                                    key={item.id}
                                    onClick={() => setActiveTab(item.id)}
                                    className={`w-full flex items-center gap-3 px-3 py-2.5 rounded-lg text-left transition-all duration-200 group ${
                                        activeTab === item.id
                                            ? "bg-gradient-to-r from-purple-500/10 to-blue-500/10 text-purple-700 dark:text-purple-300 border border-purple-200 dark:border-purple-700"
                                            : "hover:bg-gray-100 dark:hover:bg-gray-700 text-gray-700 dark:text-gray-300"
                                    }`}
                                >
                                    <Icon className="w-5 h-5" />
                                    <span className="font-medium">{item.label}</span>
                                    {item.count !== undefined && item.count > 0 && (
                                        <Badge variant="secondary" className="ml-auto text-xs">
                                            {item.count}
                                        </Badge>
                                    )}
                                </button>
                            )
                        })}
                    </nav>
                </div>

                {/* Main Content */}
                <div className="flex-1 p-6 overflow-auto h-[calc(100vh-73px)]">
                    {activeTab === "home" && (
                        <div className="space-y-6">
                            <div className="text-center py-8">
                                <h2 className="text-3xl font-bold mb-4 bg-gradient-to-r from-purple-600 to-blue-600 bg-clip-text text-transparent">
                                    Welcome to Your Workspace
                                </h2>
                                <p className="text-gray-600 dark:text-gray-400 text-lg">
                                    Organize your thoughts, tasks, and ideas in one beautiful place
                                </p>
                            </div>

                            <div className="grid md:grid-cols-2 gap-6">
                                {/* Quick Stats */}
                                <Card className="border-0 shadow-lg bg-gradient-to-br from-purple-50 to-blue-50 dark:from-purple-900/20 dark:to-blue-900/20">
                                    <CardHeader>
                                        <CardTitle className="flex items-center gap-2">
                                            <FileText className="w-5 h-5 text-purple-600" />
                                            Notes Overview
                                        </CardTitle>
                                    </CardHeader>
                                    <CardContent>
                                        <div className="text-3xl font-bold text-purple-600 mb-2">{notes.length}</div>
                                        <p className="text-gray-600 dark:text-gray-400">Total notes created</p>
                                    </CardContent>
                                </Card>

                                <Card className="border-0 shadow-lg bg-gradient-to-br from-green-50 to-emerald-50 dark:from-green-900/20 dark:to-emerald-900/20">
                                    <CardHeader>
                                        <CardTitle className="flex items-center gap-2">
                                            <CheckSquare className="w-5 h-5 text-green-600" />
                                            Tasks Progress
                                        </CardTitle>
                                    </CardHeader>
                                    <CardContent>
                                        <div className="text-3xl font-bold text-green-600 mb-2">
                                            {tasks.filter((t) => t.completed).length}/{tasks.length}
                                        </div>
                                        <p className="text-gray-600 dark:text-gray-400">Tasks completed</p>
                                    </CardContent>
                                </Card>
                            </div>

                            {/* Recent Activity */}
                            <Card className="border-0 shadow-lg">
                                <CardHeader>
                                    <CardTitle>Recent Activity</CardTitle>
                                </CardHeader>
                                <CardContent>
                                    <div className="space-y-3">
                                        {[...notes.slice(0, 2), ...tasks.slice(0, 2)].map((item, index) => (
                                            <div key={index} className="flex items-center gap-3 p-3 rounded-lg bg-gray-50 dark:bg-gray-800">
                                                {"content" in item ? (
                                                    <FileText className="w-4 h-4 text-purple-500" />
                                                ) : (
                                                    <CheckSquare className="w-4 h-4 text-green-500" />
                                                )}
                                                <span className="flex-1">{"content" in item ? item.title : item.title}</span>
                                                <span className="text-xs text-gray-500">{item.createdAt.toLocaleDateString()}</span>
                                            </div>
                                        ))}
                                    </div>
                                </CardContent>
                            </Card>
                        </div>
                    )}

                    {activeTab === "notes" && (
                        <div className="space-y-6">
                            <div className="flex items-center justify-between">
                                <h2 className="text-2xl font-bold">Notes</h2>
                                <div className="flex gap-2">
                                    <Input
                                        placeholder="New note title..."
                                        value={newNoteTitle}
                                        onChange={(e) => setNewNoteTitle(e.target.value)}
                                        onKeyPress={(e) => e.key === "Enter" && addNote()}
                                        className="w-64"
                                    />
                                    <Button onClick={addNote} className="bg-gradient-to-r from-purple-500 to-blue-600">
                                        <Plus className="w-4 h-4 mr-2" />
                                        Add Note
                                    </Button>
                                </div>
                            </div>

                            <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
                                {notes.map((note) => (
                                    <Card key={note.id} className="border-0 shadow-lg hover:shadow-xl transition-all duration-300 group">
                                        <CardHeader className="pb-3">
                                            <div className="flex items-start justify-between">
                                                <CardTitle className="text-lg group-hover:text-purple-600 transition-colors">
                                                    {note.title}
                                                </CardTitle>
                                                <div className="flex gap-1 opacity-0 group-hover:opacity-100 transition-opacity">
                                                    <Button variant="ghost" size="sm" onClick={() => setEditingNote(note.id)}>
                                                        <Edit3 className="w-4 h-4" />
                                                    </Button>
                                                    <Button variant="ghost" size="sm" onClick={() => deleteNote(note.id)}>
                                                        <Trash2 className="w-4 h-4 text-red-500" />
                                                    </Button>
                                                </div>
                                            </div>
                                        </CardHeader>
                                        <CardContent>
                                            <p className="text-gray-600 dark:text-gray-400 text-sm line-clamp-3">{note.content}</p>
                                            <div className="mt-3 text-xs text-gray-500">{note.createdAt.toLocaleDateString()}</div>
                                        </CardContent>
                                    </Card>
                                ))}
                            </div>
                        </div>
                    )}

                    {activeTab === "tasks" && (
                        <div className="space-y-6">
                            <div className="flex items-center justify-between">
                                <h2 className="text-2xl font-bold">Tasks</h2>
                                <div className="flex gap-2">
                                    <Input
                                        placeholder="New task..."
                                        value={newTaskTitle}
                                        onChange={(e) => setNewTaskTitle(e.target.value)}
                                        onKeyPress={(e) => e.key === "Enter" && addTask()}
                                        className="w-64"
                                    />
                                    <Button onClick={addTask} className="bg-gradient-to-r from-green-500 to-emerald-600">
                                        <Plus className="w-4 h-4 mr-2" />
                                        Add Task
                                    </Button>
                                </div>
                            </div>

                            <div className="space-y-3">
                                {tasks.map((task) => (
                                    <Card key={task.id} className="border-0 shadow-md hover:shadow-lg transition-all duration-200 group">
                                        <CardContent className="p-4">
                                            <div className="flex items-center gap-3">
                                                <Checkbox
                                                    checked={task.completed}
                                                    onCheckedChange={() => toggleTask(task.id)}
                                                    className="data-[state=checked]:bg-green-500 data-[state=checked]:border-green-500"
                                                />
                                                <span className={`flex-1 ${task.completed ? "line-through text-gray-500" : ""}`}>
                          {task.title}
                        </span>
                                                <div className="flex gap-1 opacity-0 group-hover:opacity-100 transition-opacity">
                                                    <Button variant="ghost" size="sm" onClick={() => deleteTask(task.id)}>
                                                        <Trash2 className="w-4 h-4 text-red-500" />
                                                    </Button>
                                                </div>
                                            </div>
                                        </CardContent>
                                    </Card>
                                ))}
                            </div>
                        </div>
                    )}
                </div>
            </div>
        </div>
    )
}
