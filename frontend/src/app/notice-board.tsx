"use client"

import { useState, useEffect } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { Skeleton } from "@/components/ui/skeleton"
import { Alert, AlertDescription } from "@/components/ui/alert"
import { Calendar, Bell, ChevronDown, Loader2, AlertCircle, User, Search, X } from "lucide-react"

interface Notice {
    id?: number
    title: string
    content: string
    author: string
    date?: string
}

export default function NoticeBoard() {
    const [notices, setNotices] = useState<Notice[]>([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)
    const [loadingMore, setLoadingMore] = useState(false)
    const [searchQuery, setSearchQuery] = useState("")
    const [filteredNotices, setFilteredNotices] = useState<Notice[]>([])

    // 공지사항 데이터 로드
    const loadNotices = async () => {
        try {
            setLoading(true)
            setError(null)

            // 실제 API 호출 대신 샘플 데이터 사용 (API 연결 시 주석 해제)
            /*
            const response = await fetch("http://localhost:8087/api/notice", {
              method: "POST",
              headers: {
                "Content-Type": "application/json",
              },
              body: JSON.stringify({}),
            })

            if (!response.ok) {
              throw new Error("공지사항을 불러오는데 실패했습니다.")
            }

            const data = await response.json()
            */

            // 샘플 데이터
            const data: Notice[] = [
                {
                    id: 1,
                    title: "시스템 정기 점검 안내",
                    content:
                        "안녕하세요. 시스템 안정성 향상을 위한 정기 점검을 실시합니다. 점검 시간 동안 서비스 이용에 불편을 드려 죄송합니다.",
                    author: "관리자",
                    date: "2024-01-15T09:00:00Z",
                },
                {
                    id: 2,
                    title: "새로운 기능 업데이트 소식",
                    content:
                        "사용자 편의성 개선을 위한 새로운 기능들이 추가되었습니다. 대시보드 UI 개선, 알림 기능 강화, 검색 성능 향상 등의 업데이트가 포함되어 있습니다.",
                    author: "개발팀",
                    date: "2024-01-12T14:30:00Z",
                },
                {
                    id: 3,
                    title: "보안 정책 변경 안내",
                    content:
                        "보안 강화를 위해 비밀번호 정책이 변경됩니다. 8자리 이상, 특수문자 포함 필수로 변경되니 참고 부탁드립니다.",
                    author: "보안팀",
                    date: "2024-01-10T11:15:00Z",
                },
                {
                    id: 4,
                    title: "고객 지원 시간 연장 안내",
                    content:
                        "더 나은 고객 서비스 제공을 위해 고객 지원 시간을 평일 오후 6시까지 연장 운영합니다. 많은 이용 부탁드립니다.",
                    author: "고객지원팀",
                    date: "2024-01-08T16:45:00Z",
                },
                {
                    id: 5,
                    title: "데이터 백업 완료 보고",
                    content:
                        "월간 정기 데이터 백업이 성공적으로 완료되었습니다. 모든 사용자 데이터가 안전하게 보관되고 있습니다.",
                    author: "시스템관리자",
                    date: "2024-01-05T02:00:00Z",
                },
            ]

            const sortedNotices = sortNoticesByDateDesc(data)
            setNotices(sortedNotices)
        } catch (err) {
            setError(err instanceof Error ? err.message : "알 수 없는 오류가 발생했습니다.")
        } finally {
            setLoading(false)
        }
    }

    // 검색 기능
    const handleSearch = (query: string) => {
        setSearchQuery(query)

        if (!query.trim()) {
            setFilteredNotices(notices)
            return
        }

        const filtered = notices.filter(
            (notice) =>
                notice.title.toLowerCase().includes(query.toLowerCase()) ||
                notice.content.toLowerCase().includes(query.toLowerCase()) ||
                notice.author.toLowerCase().includes(query.toLowerCase()),
        )

        setFilteredNotices(filtered)
    }

    // 검색어 초기화
    const clearSearch = () => {
        setSearchQuery("")
        setFilteredNotices(notices)
    }

    // 날짜 기준 내림차순 정렬
    const sortNoticesByDateDesc = (notices: Notice[]) => {
        return [...notices].sort((a, b) => {
            if (!a.date) return 1
            if (!b.date) return -1
            return new Date(b.date).getTime() - new Date(a.date).getTime()
        })
    }

    // 날짜 포맷팅
    const formatDate = (dateString?: string) => {
        if (!dateString) return ""

        try {
            const date = new Date(dateString)
            return date.toLocaleDateString("ko-KR", {
                year: "numeric",
                month: "2-digit",
                day: "2-digit",
            })
        } catch {
            return dateString
        }
    }

    // 더보기 버튼 클릭
    const handleLoadMore = async () => {
        setLoadingMore(true)
        // 실제 구현에서는 페이지네이션 로직 추가
        setTimeout(() => {
            setLoadingMore(false)
            // 임시로 알림 표시
            alert("추가 공지사항을 불러옵니다.")
        }, 1000)
    }

    useEffect(() => {
        loadNotices()
    }, [])

    useEffect(() => {
        setFilteredNotices(notices)
    }, [notices])

    return (
        <div className="min-h-screen bg-gradient-to-br from-blue-50 via-indigo-50 to-white">
            <div className="container mx-auto px-4 py-8 max-w-4xl">
                {/* 헤더 */}
                <div className="text-center mb-8">
                    <div className="flex items-center justify-center gap-3 mb-4">
                        <div className="p-3 bg-blue-100 rounded-full">
                            <Bell className="w-8 h-8 text-blue-600" />
                        </div>
                        <h1 className="text-4xl font-bold text-gray-900">공지사항</h1>
                    </div>
                    <p className="text-gray-600 text-lg">최신 소식과 중요한 공지를 확인하세요</p>
                </div>

                {/* 검색창 */}
                <div className="mb-6">
                    <Card className="shadow-md border-0 bg-white/90 backdrop-blur-sm">
                        <CardContent className="p-4">
                            <div className="relative">
                                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
                                <input
                                    type="text"
                                    placeholder="제목, 내용, 작성자로 검색..."
                                    value={searchQuery}
                                    onChange={(e) => handleSearch(e.target.value)}
                                    className="w-full pl-12 pr-12 py-4 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent bg-white hover:bg-gray-50 transition-colors text-base"
                                />
                                {searchQuery && (
                                    <button
                                        onClick={clearSearch}
                                        className="absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-400 hover:text-gray-600 transition-colors"
                                    >
                                        <X className="w-5 h-5" />
                                    </button>
                                )}
                            </div>
                            {searchQuery && (
                                <p className="text-sm text-gray-500 mt-3">
                                    "{searchQuery}"에 대한 검색 결과 {filteredNotices.length}개
                                </p>
                            )}
                        </CardContent>
                    </Card>
                </div>

                {/* 메인 컨텐츠 */}
                <Card className="shadow-lg border-0 bg-white/80 backdrop-blur-sm">
                    <CardHeader className="pb-4">
                        <CardTitle className="flex items-center gap-2 text-xl">
                            <Calendar className="w-5 h-5 text-blue-600" />
                            전체 공지사항
                            {(searchQuery ? filteredNotices : notices).length > 0 && (
                                <Badge variant="secondary" className="ml-2">
                                    {searchQuery ? filteredNotices.length : notices.length}개
                                </Badge>
                            )}
                        </CardTitle>
                    </CardHeader>

                    <CardContent className="space-y-4">
                        {/* 로딩 상태 */}
                        {loading && (
                            <div className="space-y-4">
                                {[...Array(3)].map((_, i) => (
                                    <div key={i} className="space-y-3">
                                        <div className="flex items-center gap-2">
                                            <Skeleton className="h-4 w-24" />
                                        </div>
                                        <Skeleton className="h-16 w-full" />
                                    </div>
                                ))}
                            </div>
                        )}

                        {/* 에러 상태 */}
                        {error && (
                            <Alert variant="destructive">
                                <AlertCircle className="h-4 w-4" />
                                <AlertDescription>{error}</AlertDescription>
                            </Alert>
                        )}

                        {/* 공지사항 목록 */}
                        {!loading && !error && (searchQuery ? filteredNotices : notices).length === 0 && (
                            <div className="text-center py-12">
                                <Bell className="w-16 h-16 text-gray-300 mx-auto mb-4" />
                                <p className="text-gray-500 text-lg">
                                    {searchQuery ? `"${searchQuery}"에 대한 검색 결과가 없습니다.` : "등록된 공지사항이 없습니다."}
                                </p>
                            </div>
                        )}

                        {!loading && !error && (searchQuery ? filteredNotices : notices).length > 0 && (
                            <div className="space-y-4">
                                {(searchQuery ? filteredNotices : notices).map((notice, index) => (
                                    <div
                                        key={notice.id || index}
                                        className="group p-5 rounded-lg border border-gray-200 hover:border-blue-300 hover:shadow-md transition-all duration-200 bg-white"
                                    >
                                        {/* 제목 */}
                                        <h3 className="text-lg font-semibold text-gray-900 mb-3 group-hover:text-blue-700 transition-colors">
                                            {notice.title}
                                        </h3>

                                        {/* 내용 */}
                                        <p className="text-gray-700 leading-relaxed mb-4 text-sm">{notice.content}</p>

                                        {/* 메타 정보 */}
                                        <div className="flex items-center justify-between text-xs text-gray-500 pt-3 border-t border-gray-100">
                                            <div className="flex items-center gap-4">
                                                <div className="flex items-center gap-1">
                                                    <Calendar className="w-3 h-3" />
                                                    <span>{formatDate(notice.date) || "날짜 미정"}</span>
                                                </div>
                                                <div className="flex items-center gap-1">
                                                    <User className="w-3 h-3" />
                                                    <span>{notice.author}</span>
                                                </div>
                                            </div>
                                            <span className="text-gray-400">#{notice.id || index + 1}</span>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        )}

                        {/* 더보기 버튼 */}
                        {!loading && !error && (searchQuery ? filteredNotices : notices).length > 0 && (
                            <div className="flex justify-center pt-6">
                                <Button
                                    onClick={handleLoadMore}
                                    disabled={loadingMore}
                                    variant="outline"
                                    size="lg"
                                    className="bg-white hover:bg-blue-50 border-blue-200 text-blue-700 hover:text-blue-800"
                                >
                                    {loadingMore ? (
                                        <>
                                            <Loader2 className="w-4 h-4 mr-2 animate-spin" />
                                            로딩 중...
                                        </>
                                    ) : (
                                        <>
                                            <ChevronDown className="w-4 h-4 mr-2" />더 보기
                                        </>
                                    )}
                                </Button>
                            </div>
                        )}
                    </CardContent>
                </Card>

                {/* 푸터 */}
                <div className="text-center mt-8 text-gray-500 text-sm">
                    <p>© 2024 두루미. 모든 권리 보유.</p>
                </div>
            </div>
        </div>
    )
}
