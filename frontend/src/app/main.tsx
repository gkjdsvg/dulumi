"use client"

import { MapPin, Bell, Search, ArrowRight } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { Input } from "@/components/ui/input"

export default function Component() {


    const map = () => {
        window.location.href = "http://localhost:8087/map"
    }

    const goToSignUp = () => {
        window.location.href = "http://localhost:8087/login"
    }

    const goToLogin = () => {
        window.location.href = "http://localhost:8087/login"
    }

    return (
        <div className="min-h-screen bg-white">
            {/* Header */}
            <header className="px-6 py-6 border-b border-white">
                <div className="max-w-7xl mx-auto">
                    <div className="flex items-center justify-between">
                        <div className="flex items-center space-x-3">
                            <div
                                className="w-10 h-10 bg-[#DCD3FF] rounded-2xl flex items-center justify-center">
                                <MapPin className="w-6 h-6 text-black"/>
                            </div>
                            <h1 className="text-2xl font-bold text-black">CampusMap</h1>
                        </div>
                        <div className="flex items-center space-x-4">
                            {/*<Button variant="ghost" className="text-black hover:text-black hover:bg-white">*/}
                            {/*    서비스 소개*/}
                            {/*</Button>*/}
                            {/*<Button variant="ghost" className="text-black hover:text-black hover:bg-white">*/}
                            {/*    공지사항*/}
                            {/*</Button>*/}
                            {/* 6. 로그인 - 소프트 바이올렛 배경, 호버시 흰색 배경에 소프트 바이올렛 테두리 */}
                            <Button onClick={goToLogin}
                                    className="bg-[#DCD3FF] text-black hover:bg-white hover:border-[#DCD3FF] border border-transparent">
                                로그인
                            </Button>
                            {/* 6. 회원가입 - 소프트 바이올렛 배경, 호버시 흰색 배경에 소프트 바이올렛 테두리 */}
                            <Button onClick={goToSignUp}
                                    className="bg-[#DCD3FF] text-black hover:bg-white hover:border-[#DCD3FF] border border-transparent">
                                회원가입
                            </Button>
                        </div>
                    </div>
                </div>
            </header>

            {/* Hero Section */}
            <section className="px-6 py-20 bg-white">
                <div className="max-w-7xl mx-auto">
                    <div className="text-center mb-16">
                        {/* 5. 학교 주변 전용 지도 서비스 - 호버시 흰색 배경에 소프트 바이올렛 테두리 */}
                        <Badge
                            className="bg-[#DCD3FF] text-black px-4 py-2 rounded-full mb-8 text-sm font-medium hover:bg-white hover:border-[#DCD3FF] border border-transparent transition-all">
                            🎓 학교 주변 전용 지도 서비스
                        </Badge>
                        <h1 className="text-6xl md:text-7xl font-bold text-black mb-8 leading-tight">
                            캠퍼스 주변을
                            <br/>
                            <span className="text-[#C8A2C8]">한눈에</span>
                        </h1>
                        <p className="text-xl text-black mb-12 max-w-2xl mx-auto leading-relaxed">
                            학교 주변의 모든 정보를 실시간으로 확인하고,
                            <br/>
                            친구들과 함께 공유해보세요
                        </p>
                        <div
                            className="flex flex-col sm:flex-row items-center justify-center space-y-4 sm:space-y-0 sm:space-x-6 mb-16">
                            {/* 1. 지도 둘러보기 - 하얀색 배경, 라일락 테두리 */}
                            <Button
                                className="bg-white border-[#C8A2C8] border text-black hover:bg-white h-14 px-8 text-lg rounded-2xl shadow-lg">
                                <MapPin className="w-5 h-5 mr-2"/>
                                지도 둘러보기
                            </Button>
                        </div>
                        {/* Stats */}
                    </div>
                </div>
            </section>

            {/* Map Preview Section */}
            <section className="px-6 py-20 bg-white">
                <div className="max-w-7xl mx-auto">
                    <div className="grid grid-cols-1 lg:grid-cols-2 gap-16 items-center">
                        <div>
                            {/* 5. 실시간 지도 - 호버시 흰색 배경에 소프트 바이올렛 테두리 */}
                            <Badge
                                className="bg-[#DCD3FF] text-black px-3 py-1 rounded-full mb-6 text-sm hover:bg-white hover:border-[#DCD3FF] border border-transparent transition-all">
                                실시간 지도
                            </Badge>
                            <h2 className="text-4xl font-bold text-black mb-6">
                                학교 주변 모든 정보를
                                <br/>
                                실시간으로
                            </h2>
                            <p className="text-lg text-black mb-8 leading-relaxed">
                                맛집, 카페, 편의점, 도서관까지.
                                <br/>
                                캠퍼스 라이프에 필요한 모든 장소를 한 번에 확인하세요.
                            </p>

                            <div className="space-y-4 mb-8">
                                {/* 7. 모든 동그라미 라일락으로 통일 */}
                                <div className="flex items-center space-x-3">
                                    <div className="w-2 h-2 bg-[#C8A2C8] rounded-full"></div>
                                    <span className="text-black">실시간 영업시간 확인</span>
                                </div>
                                <div className="flex items-center space-x-3">
                                    <div className="w-2 h-2 bg-[#C8A2C8] rounded-full"></div>
                                    <span className="text-black">학생 할인 정보 제공</span>
                                </div>
                                <div className="flex items-center space-x-3">
                                    <div className="w-2 h-2 bg-[#C8A2C8] rounded-full"></div>
                                    <span className="text-black">친구들과 위치 공유</span>
                                </div>
                            </div>
                            {/* 2. 지도 보기 - 호버시 하얀색 */}
                            <Button
                                onClick={map}
                                className="bg-[#DCD3FF] hover:bg-white hover:border-[#DCD3FF] border border-transparent text-black h-12 px-6 rounded-xl transition-all">
                                지도 보기
                                <ArrowRight className="w-4 h-4 ml-2"/>
                            </Button>
                        </div>
                        <div className="relative">
                            {/* Map Container */}
                            <div
                                className="bg-white rounded-3xl shadow-2xl overflow-hidden border border-[#DCD3FF]">
                                {/* Map Header */}
                                <div className="p-6 border-b border-[#DCD3FF]">
                                    <div className="flex items-center justify-between">
                                        <div className="flex items-center space-x-3">
                                            <div
                                                className="w-3 h-3 bg-[#C8A2C8] rounded-full animate-pulse"></div>
                                            <span className="font-semibold text-black">실시간 지도</span>
                                        </div>
                                        <div className="flex items-center space-x-2">
                                            {/* 4. 필터 - 소프트 바이올렛 배경, 호버시 하얀색 */}
                                            <Button
                                                size="sm"
                                                className="bg-[#DCD3FF] hover:bg-white hover:border-[#DCD3FF] border border-transparent h-8 px-3 text-xs text-black transition-all"
                                            >
                                                필터
                                            </Button>
                                            {/* 4. 검색 - 소프트 바이올렛 배경, 호버시 하얀색 */}
                                            <Button
                                                size="sm"
                                                className="bg-[#DCD3FF] hover:bg-white hover:border-[#DCD3FF] border border-transparent text-black h-8 px-3 text-xs transition-all"
                                            >
                                                검색
                                            </Button>
                                        </div>
                                    </div>
                                </div>
                                {/* Map Content */}
                                <div className="h-80 bg-white relative">
                                    <div className="absolute inset-0 flex items-center justify-center">
                                        <div className="text-center">
                                            <div
                                                className="w-16 h-16 bg-[#DCD3FF] rounded-full flex items-center justify-center mx-auto mb-4">
                                                <MapPin className="w-8 h-8 text-black"/>
                                            </div>
                                            <h4 className="text-lg font-semibold text-black mb-2">우리 학교
                                                지도</h4>
                                            <p className="text-black text-sm">실제 지도가 여기에 표시됩니다</p>
                                        </div>
                                    </div>
                                    {/* Location Pins */}
                                    <div
                                        className="absolute top-16 left-16 w-4 h-4 bg-[#C8A2C8] rounded-full animate-bounce shadow-lg"></div>
                                    <div
                                        className="absolute top-24 right-20 w-4 h-4 bg-[#DCD3FF] rounded-full animate-bounce shadow-lg"
                                        style={{animationDelay: "0.5s"}}
                                    ></div>
                                    <div
                                        className="absolute bottom-20 left-24 w-4 h-4 bg-[#C8A2C8] rounded-full animate-bounce shadow-lg"
                                        style={{animationDelay: "1s"}}
                                    ></div>
                                </div>
                                {/* Search Bar */}
                                <div className="p-4 border-t border-[#DCD3FF]">
                                    <div className="relative">
                                        <Search
                                            className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-black"/>
                                        <Input
                                            placeholder="장소를 검색해보세요..."
                                            className="pl-10 h-10 bg-white border-[#DCD3FF] text-sm focus:border-[#C8A2C8] text-black"
                                        />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                            </div>
            </section>
            {/* Features Section */}
            <section className="px-6 py-20 bg-white">
                <div className="max-w-7xl mx-auto">
                    <div className="text-center mb-16">
                        <h2 className="text-4xl font-bold text-black mb-6">캠퍼스 라이프를 더 편리하게</h2>
                        <p className="text-lg text-black max-w-2xl mx-auto">
                            학교 생활에 필요한 모든 기능을 하나의 플랫폼에서 만나보세요
                        </p>
                    </div>
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
                        {/* 2. 실시간 지도 - 아이콘 소프트 바이올렛, 호버시 하얀색 */}
                        <div
                            className="text-center p-8 rounded-3xl bg-white border border-[#DCD3FF] hover:bg-white transition-all duration-300">
                            <div
                                className="w-16 h-16 bg-white border border-[#C8A2C8] rounded-2xl flex items-center justify-center mx-auto mb-6">
                                <MapPin className="w-8 h-8 text-[#DCD3FF]"/>
                            </div>
                            <h3 className="text-xl font-bold text-black mb-4">실시간 지도</h3>
                            <p className="text-black leading-relaxed">
                                학교 주변의 모든 장소를 실시간으로 확인하고 최적의 경로를 찾아보세요
                            </p>
                        </div>
                        {/* 2. 공지사항 - 아이콘 소프트 바이올렛, 호버시 하얀색 */}
                        <div
                            className="text-center p-8 rounded-3xl bg-white border border-[#C8A2C8] hover:bg-white transition-all duration-300">
                            <div
                                className="w-16 h-16 bg-white border border-[#C8A2C8] rounded-2xl flex items-center justify-center mx-auto mb-6">
                                <Bell className="w-8 h-8 text-[#DCD3FF]"/>
                            </div>
                            <h3 className="text-xl font-bold text-black mb-4">공지사항</h3>
                            <p className="text-black leading-relaxed">학교와 주변 상점의 중요한 공지사항을 놓치지 않고 확인하세요</p>
                        </div>
                        {/* 2. 스마트 검색 - 아이콘 소프트 바이올렛, 호버시 하얀색 */}
                        <div
                            className="text-center p-8 rounded-3xl bg-white border border-[#DCD3FF] hover:bg-white transition-all duration-300">
                            <div
                                className="w-16 h-16 bg-white border border-[#C8A2C8] rounded-2xl flex items-center justify-center mx-auto mb-6">
                                <Search className="w-8 h-8 text-[#DCD3FF]"/>
                            </div>
                            <h3 className="text-xl font-bold text-black mb-4">스마트 검색</h3>
                            <p className="text-black leading-relaxed">원하는 장소를 빠르게 찾고 상세 정보를 한눈에 확인하세요</p>
                        </div>
                    </div>
                </div>
            </section>
            {/* CTA Section */}
            <section className="px-6 py-20 bg-[#DCD3FF]">
                <div className="max-w-4xl mx-auto text-center">
                    <h2 className="text-4xl font-bold text-black mb-6">지금 바로 시작해보세요</h2>
                    <p className="text-xl text-black mb-12">캠퍼스 라이프가 더욱 편리해집니다</p>
                    <div className="flex flex-col sm:flex-row items-center justify-center space-y-4 sm:space-y-0 sm:space-x-6"><Button
                        className="bg-[#C8A2C8] text-black hover:bg-white hover:border-[#C8A2C8] border border-transparent h-14 px-8 text-lg rounded-2xl font-semibold shadow-lg transition-all">
                        무료로 시작하기
                    </Button>
                        <Button
                            className="bg-[#C8A2C8] text-black hover:bg-white hover:border-[#C8A2C8] border border-transparent h-14 px-8 text-lg rounded-2xl transition-all">
                            더 알아보기
                        </Button>
                    </div>
                </div>
            </section>
            {/* Footer */}
            <footer className="px-6 py-12 bg-black">
                <div className="max-w-7xl mx-auto">
                    <div className="flex items-center justify-between">
                        <div className="flex items-center space-x-3">
                            <div
                                className="w-8 h-8 bg-[#DCD3FF] rounded-xl flex items-center justify-center">
                                <MapPin className="w-5 h-5 text-black"/>
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