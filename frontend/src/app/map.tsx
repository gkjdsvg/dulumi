"use client"

import {
    MapPin,
    Search,
    Filter,
    Star,
    Clock,
    ChevronDown,
    ChevronUp,
} from "lucide-react"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { Input } from "@/components/ui/input"
import { Card, CardContent } from "@/components/ui/card"
import { useState, useEffect, useRef } from "react"

// 타입 정의 추가
interface Place {
    id: number
    name: string
    category: string
    rating: number
    distance: string
    status: string
    hours: string
    phone: string
    discount: string
    position: { top: string; left: string }
}

export default function Component() {
    const [selectedPlace, setSelectedPlace] = useState<Place | null>(null)
    const [sidebarOpen, setSidebarOpen] = useState(true)

    const mapRef = useRef<HTMLDivElement>(null)
    const kakaoMapRef = useRef<any>(null) // 카카오 지도 객체 저장

    const places: Place[] = [
        {
            id: 1,
            name: "스타벅스 대학로점",
            category: "카페",
            rating: 4.5,
            distance: "50m",
            status: "영업중",
            hours: "07:00 - 22:00",
            phone: "02-1234-5678",
            discount: "학생 10% 할인",
            position: { top: "20%", left: "30%" },
        },
        {
            id: 2,
            name: "맘스터치 캠퍼스점",
            category: "음식점",
            rating: 4.2,
            distance: "120m",
            status: "영업중",
            hours: "10:00 - 23:00",
            phone: "02-2345-6789",
            discount: "학생 15% 할인",
            position: { top: "40%", left: "60%" },
        },
        {
            id: 3,
            name: "CU 편의점",
            category: "편의점",
            rating: 4.0,
            distance: "80m",
            status: "24시간",
            hours: "24시간 운영",
            phone: "02-3456-7890",
            discount: "학생 5% 할인",
            position: { top: "60%", left: "25%" },
        },
        {
            id: 4,
            name: "중앙도서관",
            category: "도서관",
            rating: 4.8,
            distance: "200m",
            status: "개방중",
            hours: "09:00 - 22:00",
            phone: "02-4567-8901",
            discount: "무료 이용",
            position: { top: "30%", left: "70%" },
        },
    ]

    useEffect(() => {
        const script = document.createElement("script")
        script.src = "/kakaoMap.js"
        script.async = true
        script.onload = () => {
            // @ts-ignore
            window.loadKakaoMap("kakao-map")
        }
        document.body.appendChild(script)
    }, [])

    return (
        <div className="min-h-screen bg-white">
            {/* Header */}
            <header className="px-6 py-6 border-b border-white relative z-50">
                <div className="max-w-7xl mx-auto">
                    <div className="flex items-center justify-between">
                        <div className="flex items-center space-x-3">
                            <div className="w-10 h-10 bg-[#DCD3FF] rounded-2xl flex items-center justify-center">
                                <MapPin className="w-6 h-6 text-black" />
                            </div>
                            <h1 className="text-2xl font-bold text-black">CampusMap</h1>
                        </div>
                        <div className="flex items-center space-x-4">
                            <Button variant="ghost" className="text-black hover:text-black hover:bg-white">
                                서비스 소개
                            </Button>
                            <Button variant="ghost" className="text-black hover:text-black hover:bg-white">
                                공지사항
                            </Button>
                            <Button className="bg-[#DCD3FF] text-black hover:bg-white hover:border-[#DCD3FF] border border-transparent">
                                로그인
                            </Button>
                            <Button className="bg-[#DCD3FF] text-black hover:bg-white hover:border-[#DCD3FF] border border-transparent">
                                회원가입
                            </Button>
                        </div>
                    </div>
                </div>
            </header>

            <div className="flex h-screen">
                {/* Sidebar */}
                <div
                    className={`${sidebarOpen ? "w-96" : "w-0"} transition-all duration-300 bg-white border-r border-[#DCD3FF] overflow-hidden relative z-40`}
                >
                    <div className="p-6 h-full overflow-y-auto">
                        {/* Search Section */}
                        <div className="mb-6">
                            <div className="relative mb-4">
                                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-black" />
                                <Input
                                    placeholder="장소를 검색하세요..."
                                    className="pl-10 h-12 bg-white border-[#DCD3FF] focus:border-[#C8A2C8] text-black rounded-xl"
                                />
                            </div>

                            {/* Filter Buttons */}
                            <div className="flex flex-wrap gap-2 mb-4">
                                <Button className="bg-[#C8A2C8] text-black hover:bg-white hover:border-[#C8A2C8] border border-transparent px-4 py-2 text-sm rounded-lg transition-all">
                                    전체
                                </Button>
                                <Button className="bg-[#DCD3FF] hover:bg-white hover:border-[#DCD3FF] border border-transparent text-black px-4 py-2 text-sm rounded-lg transition-all">
                                    카페
                                </Button>
                                <Button className="bg-[#DCD3FF] hover:bg-white hover:border-[#DCD3FF] border border-transparent text-black px-4 py-2 text-sm rounded-lg transition-all">
                                    음식점
                                </Button>
                                <Button className="bg-[#DCD3FF] hover:bg-white hover:border-[#DCD3FF] border border-transparent text-black px-4 py-2 text-sm rounded-lg transition-all">
                                    편의점
                                </Button>
                                <Button className="bg-[#DCD3FF] hover:bg-white hover:border-[#DCD3FF] border border-transparent text-black px-4 py-2 text-sm rounded-lg transition-all">
                                    도서관
                                </Button>
                            </div>

                            <Button className="w-full bg-[#DCD3FF] hover:bg-white hover:border-[#DCD3FF] border border-transparent text-black h-10 rounded-xl transition-all">
                                <Filter className="w-4 h-4 mr-2" />
                                상세 필터
                            </Button>
                        </div>

                        {/* Places List */}
                        <div className="space-y-4">
                            <h3 className="text-lg font-bold text-black mb-4">주변 장소</h3>
                            {places.map((place) => (
                                <Card
                                    key={place.id}
                                    className={`border cursor-pointer transition-all duration-300 ${
                                        selectedPlace?.id === place.id
                                            ? "border-[#C8A2C8] shadow-lg"
                                            : "border-[#DCD3FF] hover:border-[#C8A2C8]"
                                    }`}
                                    onClick={() => setSelectedPlace(place)}
                                >
                                    <CardContent className="p-4">
                                        <div className="flex items-start justify-between mb-2">
                                            <div>
                                                <h4 className="font-bold text-black text-sm">{place.name}</h4>
                                                <div className="flex items-center space-x-2 mt-1">
                                                    <Badge className="bg-[#DCD3FF] text-black px-2 py-1 text-xs rounded-lg hover:bg-white hover:border-[#DCD3FF] border border-transparent transition-all">
                                                        {place.category}
                                                    </Badge>
                                                    <span className="text-xs text-black">{place.distance}</span>
                                                </div>
                                            </div>
                                            <div className="flex items-center space-x-1">
                                                <Star className="w-3 h-3 text-[#C8A2C8] fill-current" />
                                                <span className="text-xs text-black">{place.rating}</span>
                                            </div>
                                        </div>

                                        <div className="flex items-center space-x-4 text-xs text-black mb-2">
                                            <div className="flex items-center space-x-1">
                                                <div
                                                    className={`w-2 h-2 rounded-full ${
                                                        place.status === "영업중" || place.status === "개방중" || place.status === "24시간"
                                                            ? "bg-[#C8A2C8]"
                                                            : "bg-[#DCD3FF]"
                                                    }`}
                                                ></div>
                                                <span>{place.status}</span>
                                            </div>
                                            <div className="flex items-center space-x-1">
                                                <Clock className="w-3 h-3 text-[#C8A2C8]" />
                                                <span>{place.hours}</span>
                                            </div>
                                        </div>

                                        <div className="text-xs text-black">
                                            <span className="text-[#C8A2C8] font-medium">{place.discount}</span>
                                        </div>
                                    </CardContent>
                                </Card>
                            ))}
                        </div>
                    </div>
                </div>

                {/* Sidebar Toggle Button - 사이드바 경계에 붙이기 */}
                <Button
                    onClick={() => setSidebarOpen(!sidebarOpen)}
                    className={`fixed top-1/2 transform -translate-y-1/2 z-50 bg-[#DCD3FF] hover:bg-white hover:border-[#DCD3FF] border border-transparent text-black w-6 h-16 transition-all shadow-lg ${
                        sidebarOpen ? "left-96 rounded-r-xl border-l-0" : "left-0 rounded-r-xl"
                    }`}
                >
                    {sidebarOpen ? <ChevronUp className="w-4 h-4 rotate-90" /> : <ChevronDown className="w-4 h-4 -rotate-90" />}
                </Button>

                {/* Map Section */}
                <div className="flex-1 relative bg-white">
                    {/* Map Container - 카카오 지도 렌더링 위치 */}
                    <div
                        ref={mapRef}
                        className="w-full h-full"
                        style={{ minHeight: "600px", position: "relative" }}
                    ></div>

                    {/* 기존에 있던 지도 스타일 & 위치핀, 기타 UI는 필요없으니 제거했습니다. */}
                </div>
            </div>

            {/* Footer */}
            <footer className="px-6 py-12 bg-black">
                <div className="max-w-7xl mx-auto">
                    <div className="flex items-center justify-between">
                        <div className="flex items-center space-x-3">
                            <div className="w-8 h-8 bg-[#DCD3FF] rounded-xl flex items-center justify-center">
                                <MapPin className="w-5 h-5 text-black" />
                            </div>
                            <span className="text-xl font-bold text-white">CampusMap</span>
                        </div>
                        <div className="flex items-center space-x-8">
                            <a href="#" className="text-white hover:text-[#DCD3FF] transition-colors">
                                서비스 소개
                            </a>
                            <a href="#" className="text-white hover:text-[#DCD3FF] transition-colors">
                                공지사항
                            </a>
                            <a href="#" className="text-white hover:text-[#DCD3FF] transition-colors">
                                고객지원
                            </a>
                        </div>
                    </div>
                    <div className="mt-8 pt-8 border-t border-white text-center">
                        <p className="text-white">© 2024 CampusMap. All rights reserved.</p>
                    </div>
                </div>
            </footer>
        </div>
    )
}
